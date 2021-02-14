# ImageMatrixUtils
Image filtering in Java using convolutions and matrix math

![Original](https://i.ibb.co/d0Q3TpR/nut.jpg)
![Filtered 1](https://i.ibb.co/tZFktQk/nut3.png)

## What is this project?
- A Java library for image and matrix related math.
> Functions for reading images and converting images to matricies and back. Includes tools to preform image convolutions, strided convolutions and padded convolutions.

> Addition, subtraction, multiplication, divison, matrix flattening and more.

- A Java library for executing image filtering and executing image convolutions.
> Includes extensible filters for adding any amount of custom image filtering.
- A great resource for understanding matrix related math implementations.
> The code for this project is heavily commented.

## What I learned from this project.
- Programming paragdims and best techniques for structuring library projects.
> Not all projects are created equally. This Java project was designed to be a resource for other projects to use, and thus its interface and design impacts how the product is used. In this project I used mainly static methods for computation. In the future I would like to be able to cache some computations or provide better efficiencies.
- The math behind simple matrix ocomputations and image convolution.
> This project and the math it is based upon is used extensively in machine learning, particularly in deep learning with images. A basic deep learning network that learns from unstructured image data does so by creationg, updating and evaluating image convolutions. Understanding how convolutions work is essential in understanding how these networks "think" through the problem.
