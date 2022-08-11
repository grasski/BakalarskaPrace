
import tensorflow as tf
assert tf.__version__.startswith('2')

from tflite_model_maker import image_classifier
from tflite_model_maker.image_classifier import DataLoader


# import matplotlib.pyplot as plt
# import numpy as np
# import os
# import PIL

from tensorflow import keras
from tensorflow.keras import layers
from tensorflow.keras.models import Sequential


def main():
    images_path = "..\\animalsData\\images"

    # physical_devices = tf.config.experimental.list_physical_devices("GPU")
    # print("Num GPUs: ", len(physical_devices))
    #
    # batch_size = 2
    # img_height = 256
    # img_width = 256
    #
    # train_ds = tf.keras.utils.image_dataset_from_directory(
    #     images_path,
    #     validation_split=0.2,
    #     subset="training",
    #     seed=123,
    #     image_size=(img_height, img_width),
    #     batch_size=batch_size)
    #
    # val_ds = tf.keras.utils.image_dataset_from_directory(
    #     images_path,
    #     validation_split=0.2,
    #     subset="validation",
    #     seed=123,
    #     image_size=(img_height, img_width),
    #     batch_size=batch_size)
    #
    # class_names = train_ds.class_names
    # print(class_names)
    #
    # for image_batch, labels_batch in train_ds:
    #     print(image_batch.shape)
    #     print(labels_batch.shape)
    #     break
    #
    #
    # AUTOTUNE = tf.data.AUTOTUNE
    # train_ds = train_ds.shuffle(1000)
    # val_ds = val_ds
    # normalization_layer = layers.Rescaling(1. / 255)
    #
    # normalized_ds = train_ds.map(lambda x, y: (normalization_layer(x), y))
    # image_batch, labels_batch = next(iter(normalized_ds))
    # first_image = image_batch[0]
    # # Notice the pixel values are now in `[0,1]`.
    # print(np.min(first_image), np.max(first_image))
    #
    #
    # num_classes = len(class_names)
    #
    # model = Sequential([
    #     layers.Rescaling(1. / 255, input_shape=(img_height, img_width, 3)),
    #     layers.Conv2D(16, 3, padding='same', activation='relu'),
    #     layers.MaxPooling2D(),
    #     layers.Conv2D(32, 3, padding='same', activation='relu'),
    #     layers.MaxPooling2D(),
    #     layers.Conv2D(64, 3, padding='same', activation='relu'),
    #     layers.MaxPooling2D(),
    #     layers.Flatten(),
    #     layers.Dense(128, activation='relu'),
    #     layers.Dense(num_classes)
    # ])
    #
    # model.compile(optimizer='adam',
    #               loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
    #               metrics=['accuracy'])
    #
    # model.summary()
    #
    # epochs = 10
    # history = model.fit(
    #     train_ds,
    #     validation_data=val_ds,
    #     epochs=epochs
    # )
    #
    # model.save("my_h5_model2.h5")
    #
    # acc = history.history['accuracy']
    # val_acc = history.history['val_accuracy']
    #
    # loss = history.history['loss']
    # val_loss = history.history['val_loss']
    #
    # epochs_range = range(epochs)
    #
    # plt.figure(figsize=(8, 8))
    # plt.subplot(1, 2, 1)
    # plt.plot(epochs_range, acc, label='Training Accuracy')
    # plt.plot(epochs_range, val_acc, label='Validation Accuracy')
    # plt.legend(loc='lower right')
    # plt.title('Training and Validation Accuracy')
    #
    # plt.subplot(1, 2, 2)
    # plt.plot(epochs_range, loss, label='Training Loss')
    # plt.plot(epochs_range, val_loss, label='Validation Loss')
    # plt.legend(loc='upper right')
    # plt.title('Training and Validation Loss')
    # plt.show()

    tf.config.experimental.set_memory_growth(tf.config.list_physical_devices('GPU')[0], True)
    print("Num GPUs Available: ", len(tf.config.list_physical_devices('GPU')))

    data = DataLoader.from_folder(images_path)

    trainData, restData = data.split(0.8)
    validationData, testData = restData.split(0.5)

    modelSpec = 'efficientnet_lite3'
    model = image_classifier.create(
        train_data=trainData,
        validation_data=validationData,
        model_spec=modelSpec,
        epochs=2
    )
    # loss, accuracy = model.evaluate(testData)
    # print(loss)
    # print(accuracy)
    # model.export(export_dir='.')



# marker_mapping = {
#     0xffd8: "Start of Image",
#     0xffe0: "Application Default Header",
#     0xffdb: "Quantization Table",
#     0xffc0: "Start of Frame",
#     0xffc4: "Define Huffman Table",
#     0xffda: "Start of Scan",
#     0xffd9: "End of Image"
# }
#
#
# class JPEG:
#     def __init__(self, image_file):
#         with open(image_file, 'rb') as f:
#             self.img_data = f.read()
#
#     def decode(self):
#         data = self.img_data
#         while (True):
#             marker, = unpack(">H", data[0:2])
#             # print(marker_mapping.get(marker))
#             if marker == 0xffd8:
#                 data = data[2:]
#             elif marker == 0xffd9:
#                 return
#             elif marker == 0xffda:
#                 data = data[-2:]
#             else:
#                 lenchunk, = unpack(">H", data[2:4])
#                 data = data[2 + lenchunk:]
#             if len(data) == 0:
#                 raise TypeError("issue reading jpeg file")
#
# bads = []
#
# animals = ["Duck", "Elephant", "Flamingo", "Giraffe", "Gorilla", "Kangaroo", "Lion", "Parrot", "Penguin", "Sea lion", "Tiger", "Turtle", "Zebra"]
# for animal in animals:
#     images = f"..\\animalsData\\images\\{animal}\\*"
#     images = glob.glob(images)
#     for img in tqdm(images):
#         # image = glob.glob(images)
#         image = JPEG(img)
#         try:
#             image.decode()
#         except:
#             bads.append(img)
#
# print(bads)


if __name__ == '__main__':
    main()
