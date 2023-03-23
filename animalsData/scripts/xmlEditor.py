import os
import xml.etree.ElementTree as ET
import glob

toDelete = ["Buffalo", "Capybara", "Flamingo"]
ignore = ["buffalo", "capybara", "flamingo", "downloads", "cat", "smallImages", "ZALOHA", "zeroObjects", "WRONG", "ALL"]
pathToMove = "./WRONG/zeroObjects"

totalObjects = 0
path = os.walk(os.curdir)
#dirs = [name for name in os.listdir(".") if os.path.isdir(name)]
dirs = ["downloads/dog/2"]
for dr in dirs:
    if dr in ignore:
        print(f"Skipping {dr} folder.")
        continue

    for path in ["test", "train", "images"]:
        print(f"Start of {os.path.join(dr, path)}")
        filePaths = glob.glob(dr + f"/{path}/*.xml")
        for filePath in filePaths:
            tree = ET.parse(filePath)
            root = tree.getroot()

            imgFileName = os.path.basename(filePath)
            root.find('filename').text = imgFileName[:-3] + "jpg"

            imSize = root.find("size")
            imgWidth = int(imSize.find("width").text)
            imgHeight = int(imSize.find("height").text)

            path = root.find("path")
            if path is not None:
                root.remove(path)

            folder = root.find("folder")
            if folder is not None:
                root.remove(folder)

            source = root.find("source")
            if source is not None:
                root.remove(source)

            for ob in root.findall("object"):
                n = ob.find("name").text
                if n in toDelete:
                    print(f"Removed {n} from {filePath}")
                    root.remove(ob)
                    continue
                
                ob.find("name").text = n.capitalize()

                bndbox = ob.find("bndbox")
                if bndbox is None:
                    print(filePath, f"REMOVED object because of no bndbox element!")
                    root.remove(ob)
                    continue

                xmin = int(bndbox.find("xmin").text)
                xmax = int(bndbox.find("xmax").text)
                ymin = int(bndbox.find("ymin").text)
                ymax = int(bndbox.find("ymax").text)

                xvalue = xmax - xmin
                yvalue = ymax - ymin
                if xvalue <= 32 or yvalue <= 32:
                    print(filePath, f"REMOVED object because of xvalue={xvalue}, yvalue={yvalue}")
                    root.remove(ob)
                    continue

                if xmin < 0:
                    bndbox.find("xmin").text = "0"
                    print(f"[WARNING] Error with {filePath}, xmin {xmin} < 0")
                if xmax > imgWidth:
                    bndbox.find("xmax").text = str(imgWidth)
                    print(f"[WARNING] Error with {filePath}, xmax {xmax} > {imgWidth}")
                if ymin < 0:
                    bndbox.find("ymin").text = "0"
                    print(f"[WARNING] Error with {filePath}, ymin {ymin} < 0")
                if ymax > imgHeight:
                    bndbox.find("ymax").text = str(imgHeight)
                    print(f"[WARNING] Error with {filePath}, ymax {ymax} > {imgHeight}")

                
                pose = ob.find("pose")
                if pose is not None:
                    pose.text = "Unspecified"
                else:
                    pose = ET.Element("pose")
                    pose.text = "Unspecified"
                    ob.append(pose)

                occluded = ob.find("occluded")
                if occluded is not None:
                    ob.remove(occluded)
                polygons = ob.find("polygon")
                if polygons is not None:
                    ob.remove(polygons)
                

                totalObjects += 1
                    
            tree.write(filePath, encoding='UTF-8', xml_declaration=True)

            tree = ET.parse(filePath)
            root = tree.getroot()
            if len(root.findall("object")) == 0:
                print(f"Zero objects in {filePath}, moving out!")

                labelPath = filePath
                imagePath = filePath[:-3] + "jpg"

                labelName = os.path.basename(filePath)  
                imageName = labelName[:-3] + "jpg"

                moveImagePath = os.path.join(pathToMove, imageName)  # path to new folder for image
                moveLabelPath = os.path.join(pathToMove, labelName)  # path to new folder for XML
                print(imagePath, " MOVING TO ", moveImagePath)
                print(labelPath, " MOVING TO ", moveLabelPath)
                os.rename(imagePath, moveImagePath)
                os.rename(labelPath, moveLabelPath)

        print(dr, path, "finished.")

print("done", totalObjects)
