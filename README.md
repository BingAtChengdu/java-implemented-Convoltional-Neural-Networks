# java-implemented-Convoltional-Neural-Networks

java implemented Convoltional Neural Networks. There are a lot of deep learning networks, but they may be complicated and not friendly 
to java programmers. 

This project is  simpler for java programmers to learn and easy to use,help java programmers understanding how convolutional neural network
works.

## implemented:
1. Convolutional Neural layer
2. Maxpooling layer
3. Relu layer
4. Affine layer
5. Sigmoid layer
6. Softmax with loss layer
7. numberic gradient
8. back propagation
9. SGD update
10. Batchnorm layer
11. Dropout layer

Momentoum, Adam update will be implemented later.

## How to use:
### compile from source
1. install JDK 8 or higher
2. compile the source
3. set parameters in startup.properties
4. run a network : java run networkname
5. train a network: java train networkname

### run ai.jar
The jar file is under foler ForTest.
1. install jDK8 or higher
2. download runable jar under ForTest folder
3. all necessary resource is under ForTest folder
4. edit the startup.properties file to modify the relative path.
5. the startup.properties file must be in the same folder as ai.jar 

## startup.properties:edit this file to modify parameters 
### 通道
channel = 1
### 滤波器数量
filterNumber = 30
### 滤波器尺寸
filterSize = 3
### 卷积层堆叠层数
cnnLayers = 4
### 通道填充尺寸
pad = 0
### 滑动步长
stride = 1

### 全连接层输入神经元大小，-1代表运行时动态初始化
inputSize = -1
### 隐藏层神经元数量
hiddenSize = 100
### 输出层神经元数量
outputSize = 10

### 激活函数
activation = relu / sigmoid
### 全连接层堆叠层数
denseLayers = 2
### 是否使用dropout减少过拟合
userDropout = false

### 训练样本批大小
batchSize = 100
### 训练数据大小
trainSize = 10000
### 测试数据大小
testSize = 50
### 学习率
learningRate = 0.1d
### 迭代次数
iteNum = 1000

### 数据集路径
trainImgPath = D:/AI/mnist-data-reader-master/data/train-images.idx3-ubyte
trainLabelPath = D:/AI/mnist-data-reader-master/data/train-labels.idx1-ubyte
testImgPath = D:/AI/mnist-data-reader-master/data/t10k-images.idx3-ubyte
testLabelPath = D:/AI/mnist-data-reader-master/data/t10k-labels.idx1-ubyte

### 网络保存路径
trainSavePath=d:/AI/

