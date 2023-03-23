
import json as JS 
import xml.etree.cElementTree as e
import os

cwd = os.getcwd()

folder = os.path.join("bird_dataset", "train")
l = os.listdir(folder)
cwd_xml = folder

i = 0
for filename in list(l[1::2]):
    
    file = os.path.join(folder,filename)
    with open(file, "r") as json_file: 
        print(filename)

        # loading json file data 
        # to variable data 
        data = JS.load(json_file)
        
        r = e.Element("annotation")
        e.SubElement(r,"folder").text = "VOC2007"
        e.SubElement(r,"filename").text = filename[:-5] + ".jpg"

        source = e.SubElement(r,"source")
        e.SubElement(source,"database").text = "Coco database"

        size = e.SubElement(r,"size")
        e.SubElement(size,"width").text = str (data["imgWidth"])
        e.SubElement(size,"height").text = str (data["imgHeight"])
        e.SubElement(size,"depth").text = "3"

        e.SubElement(r,"segmented").text = "0"
 
    
        for z in data["objects"]:
            ann = e.SubElement(r,"object")
            e.SubElement(ann,"name").text = z["label"].capitalize()
            e.SubElement(ann,"pose").text = "Unspecified"
            e.SubElement(ann,"truncated").text = "0"
            e.SubElement(ann,"difficult").text = "0"
            
            bbox = e.SubElement(ann,"bndbox")
            e.SubElement(bbox,"xmin").text =str(z["boundingbox"][0])
            e.SubElement(bbox,"ymin").text =str(z["boundingbox"][1])
            e.SubElement(bbox,"xmax").text =str(z["boundingbox"][2])
            e.SubElement(bbox,"ymax").text =str(z["boundingbox"][3])

        a = e.ElementTree(r)
        f = filename[:-5]
        f = f + ".xml"
        f = cwd_xml + '\\' + f
        a.write(f)

