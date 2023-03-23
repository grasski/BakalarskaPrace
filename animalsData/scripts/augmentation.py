import os
import cv2
import numpy as np
from PIL import Image
import hashlib
import imgaug.augmenters as iaa
import imageio
import glob


animalsPath = glob.glob("images\\*")
augmentation = iaa.Sequential([
    iaa.Fliplr(0.5),
    iaa.Flipud(0.5),
    iaa.Affine(
        translate_percent={"x": (-0.2, 0.2), "y": (-0.2, 0.2)},
        rotate=(-30, 30),
        scale=(0.75, 1.3)
    ),

    iaa.Sometimes(0.5, iaa.Multiply((0.7, 1.4))),

    iaa.Sometimes(0.5, iaa.LinearContrast((0.6, 1.5)))
])

for animalPath in animalsPath:
    if "Anteater" in animalPath or "Capybara" in animalPath or "Camel" in animalPath
        or "Jaguar" in animalPath or "Rhino" in animalPath:
            imagesPath = glob.glob(os.path.join(animalPath, "*"))
            print("Images of: " + animalPath)
            rep = 2 if "Jaguar" in animalPath else 3
            for i, imgPath in enumerate(imagesPath):
                category = os.path.basename(os.path.dirname(imgPath))
                img = cv2.imread(imgPath)
                saveTo = os.path.join("..\\animalsData\\images\\", category)
                for r in range(rep-1):
                    augmImage = augmentation(image=img)

                    name = os.path.join(saveTo, f"{category}_{i}_aug_{r}.jpg")
                    cv2.imwrite(name, augmImage)

