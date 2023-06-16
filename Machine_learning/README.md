<h1 align="center">Meongku Machine Learning</h1>


## Summary
The Meongku model is used to identify 12 different cat breeds based on users' input. 

## Library
1. Numpy.
2. Matplotlib.
3. PIL.
4. TensorFlow.
5. Pathlib.
6. Os.

>><img src="images/one.jpg" width="400">

## Load Images Data
1. Specify the data directory.
2. Load images data from the data directory using `tf.keras.utils.image_dataset_from_directory`. Split into training and validation data.

>><img src="images/two.jpg" width="400">
>><img src="images/three.jpg" width="400">

3. Take 20% of validation data to test data.

>><img src="images/four.jpg" width="400">

4. Examine class names in the data.

>><img src="images/fifve.jpg" width="400">


## Create The Model using Efficient Net Version 2
1. Specify the image shape.
2. Download the “efficientnet_v2” model.

>><img src="images/six.jpg" width="400">

3. Add a batch normalization layer, dense layer, dropout layer, and output layer.

>><img src="images/seven.jpg" width="400">

4. Look at the summary of the model.

>><img src="images/eight.jpg" width="400">

5. Compile the model.

>><img src="images/nine.jpg" width="400">

6. Check loss and accuracy before training the model.
7. Fit the model with five epochs.

>><img src="images/ten.jpg" width="400">

8. Plot loss and accuracy with the epoch.

>><img src="images/eleven.jpg" width="400">


## Evaluate the Model
Evaluate the model with evaluate method.

>><img src="images/twelve.jpg" width="400">

## Save the model
Save the model using “model.save” based on the path that the model will be saved.

>><img src="images/thirteen.jpg" width="400">

## Load the model
Load the model using “tf.keras.models.load_model”, where the input is the model's path.

>><img src="images/fourteen.jpg" width="400">

## Tools
1. Visual Studio Code
2. Google Colab

## Source of Dataset
1. Kaggle


## Resository FastApi ML Model:
https://github.com/arisafriyanto/cat-breed-fastapi.git