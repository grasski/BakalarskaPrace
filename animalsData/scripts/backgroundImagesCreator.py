import os
import pathlib
import shutil
from PIL import Image
import glob
import xml.etree.ElementTree as ET


dirs = ["SeaLion", "Anteater", "Camel", "Flamingo", "Lion", "Gorilla", "Duck"]
nameId = 0
name = f"background_{nameId}"
for dr in dirs:
    filePaths = glob.glob(dr + "/*.jpg")
    i = 0
    for filePath in filePaths:
        if i >= (8000 / len(dirs)):
            break

        image = Image.open(filePath)
        image.load()
        imageSize = image.size
        width = image.width
        height = image.height
        depth = len(image.getbands())

        imgName = name + pathlib.Path(filePath).suffix
        xmlFileName = name + ".xml"

        root = ET.Element('annotation')
        filenameEt = ET.Element("filename")
        filenameEt.text = imgName
        root.append(filenameEt)

        sizeElement = ET.Element("size")
        ET.SubElement(sizeElement, "width").text = str(width)
        ET.SubElement(sizeElement, "height").text = str(height)
        ET.SubElement(sizeElement, "depth").text = str(depth)
        root.append(sizeElement)

        ET.SubElement(root, "segmented").text = str(0)

        destinationDir = "../data/labeled/zzBackground"

        tree = ET.ElementTree(root)
        tree.write(os.path.join(destinationDir, xmlFileName))
        shutil.copy(filePath, os.path.join(destinationDir, name + pathlib.Path(filePath).suffix))

        nameId += 1
        name = f"background_{nameId}"
        i += 1

    print(dr + " is done")

print("done")
