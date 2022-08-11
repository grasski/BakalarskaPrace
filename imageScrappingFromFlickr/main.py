import glob

import cv2
import flickrapi
import urllib.request
import os

import numpy as np
from PIL import Image
import hashlib
import imgaug.augmenters as iaa
import imageio

# open the .txt list of bird species and read them in as a list
# with open("..\\animalsData\\animals.txt") as birds_file:
#     animals = [line.strip() for line in birds_file]

# get an api key and secret from flickr


class ImageWorker:

    def flickrdownloader(self):
        api_key = u'2b8af11d0bc12feb4513c0c88dbe1435'
        api_secret = u'62f0041f96dbb8b7'
        flickr = flickrapi.FlickrAPI(api_key, api_secret)

        for animal in ["Rhino"]:
            print("Downloading: " + animal)

            photos = flickr.walk(text=animal,
                                 tag_mode='all',
                                 tags=animal,
                                 extras='url_c',
                                 privacy_filters=1,
                                 per_page=500,
                                 sort='relevance',
                                 content_type=1)

            checked = {}
            f = 0
            removesInRow = 0
            for i, photo in enumerate(photos):
                url = photo.get('url_c')

                testDirectory = f'..\\animalsData\\images\\{animal}'
                # check if directory exists
                if not os.path.exists(testDirectory):
                    os.makedirs(testDirectory)

                # if an error occurs just keep moving
                try:
                    filepath = os.path.join(testDirectory, f'{i + 1 - f}.jpg')

                    toRemove = False
                    urllib.request.urlretrieve(url, filepath)
                    with Image.open(filepath) as img:
                        md5 = hashlib.md5(img.tobytes()).hexdigest()
                        if md5 not in checked:
                            checked[md5] = filepath
                            removesInRow = 0
                        else:
                            print(f"Image with url: {url}, is same as: {checked[md5]}. Removing.")
                            print(f"Removes in row: {removesInRow}")
                            toRemove = True
                            removesInRow += 1

                    if toRemove:
                        f += 1
                        os.remove(filepath)

                    if removesInRow > 59:
                        print("Too many removes in row.")
                        break

                    if i > 5999 + f:
                        break
                except:
                    print(f"Image failed to download: {f}, url: {url}")
                    removesInRow = 0
                    f += 1
                    pass

            print(animal + " failed to download " + str(f) + " times.")

    def imageAugmentation(self):
        animalsPath = glob.glob("..\\animalsData\\images\\*")
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

        images = []
        for animalPath in animalsPath:
            imagesPath = glob.glob(os.path.join(animalPath, "*"))
            print("Collecting images of: " + animalPath)
            category = ""
            for imgPath in imagesPath:
                category = os.path.basename(os.path.dirname(imgPath))
                img = cv2.imread(imgPath)
                images.append(img)

            print(f"IMAGE SAVING OF CATEGORY {category} STARTED")
            reps = 2
            for r in range(reps):
                print(f"Repeat no. {r}")
                augmentedImg = augmentation(images=images)
                saveTo = os.path.join("..\\animalsData\\images\\", category)
                for i, im in enumerate(augmentedImg):
                    name = os.path.join(saveTo, f"aug_{r}_{i}.jpg")
                    cv2.imwrite(name, im)

            images = []


if __name__ == "__main__":
    worker = ImageWorker()
    worker.flickrdownloader()
    # worker.imageAugmentation()
