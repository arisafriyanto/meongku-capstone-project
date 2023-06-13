# import library yang bakal dipake
from fastapi import FastAPI, File, UploadFile
from tensorflow import keras
import numpy as np
from PIL import Image
import uvicorn

# label ras kucingnya
class_names = ['Abyssinian', 'Bengal', 'Birman', 'Bombay', 'British Shorthair', 'Egyptian Mau', 'Maine Coon', 'Persian', 'Ragdoll', 'Russian Blue', 'Siamese', 'Sphynx']

# buat instance FastAPI
app = FastAPI()

# load model TensorFlow yang udh disimpen di folder
model_path = "/app/models" # tentuin path modelnya
model = keras.models.load_model(model_path)

@app.get("/", status_code=200)
async def root():
    return {
        "healthCheck": "OK",
        "message": "Hello, Bangkit Academy"
    }

# tambahkan endpoint pada aplikasi FastAPI untuk menerima file gambar dan melakukan prediksi
@app.post("/predict", status_code=201)
async def predict(image_file: UploadFile = File(...)):
    # PROSES DI BAWAH KURANG LEBIH SAMA KAYAK BAGIAN 'PREDIKSI' DI FILE 'LOAD_MODEL.IPYNB'
    # open file gambar
    image = Image.open(image_file.file) # buka image-nya

    # preprocess gambar
    image = image.resize((224, 224))  # Sesuaikan dengan ukuran input model
    image_array = np.array(image) # dijadiin np array

    # reshape gambar menjadi bentuk yang sesuai dengan model
    image_array = np.expand_dims(image_array, axis=0)

    # prediksi
    predictions = model.predict(image_array)

    # ambil label kelas dengan probabilitas tertinggi
    predicted_class = np.argmax(predictions)
    predicted_class = class_names[predicted_class]
    
    return {"predicted_class": predicted_class}

# jalankan server FastAPI
if __name__ == "__main__":
    uvicorn.run(app, host="localhost", port=8080)
