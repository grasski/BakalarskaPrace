# https://github.com/Laudarisd/Data_preprocessing/blob/860d76b0e12576aaf0333ba215bf6f9d628201e5/csv_to_voc_xml.py

from collections import defaultdict
import os
import csv

from xml.etree.ElementTree import parse, Element, SubElement, ElementTree
import xml.etree.ElementTree as ET

save_root2 = "test_xmls"

if not os.path.exists(save_root2):
    os.mkdir(save_root2)


def write_xml(folder, filename, bbox_list):
    # Details from first entry
    e_filename, e_width, e_height, e_class_name, e_xmin, e_ymin, e_xmax, e_ymax = bbox_list[0]
    
    root = Element('annotation')
    SubElement(root, 'folder').text = ""
    SubElement(root, 'filename').text = e_class_name + filename
    SubElement(root, 'path').text = ""
    source = SubElement(root, 'source')
    SubElement(source, 'database').text = 'Unknown'

    size = SubElement(root, 'size')
    SubElement(size, 'width').text = e_width
    SubElement(size, 'height').text = e_height
    SubElement(size, 'depth').text = '3'

    SubElement(root, 'segmented').text = '0'

    for entry in bbox_list:
        e_filename, e_width, e_height, e_class_name, e_xmin, e_ymin, e_xmax, e_ymax = entry
        
        obj = SubElement(root, 'object')
        SubElement(obj, 'name').text = e_class_name
        SubElement(obj, 'pose').text = 'Unspecified'
        SubElement(obj, 'truncated').text = '0'
        SubElement(obj, 'difficult').text = '0'

        bbox = SubElement(obj, 'bndbox')
        SubElement(bbox, 'xmin').text = e_xmin
        SubElement(bbox, 'ymin').text = e_ymin
        SubElement(bbox, 'xmax').text = e_xmax
        SubElement(bbox, 'ymax').text = e_ymax

    #indent(root)
    tree = ElementTree(root)

    img_path = os.path.join("images", "test", filename)
    if (os.path.isfile(img_path)):
        new_img_path = os.path.join("images", "test", e_class_name+filename)
        os.rename(img_path, new_img_path)
  
    xml_filename = os.path.join('.', folder, os.path.splitext(e_class_name+filename)[0] + '.xml')
    tree.write(xml_filename)
    

entries_by_filename = defaultdict(list)

with open('train.csv', 'r', encoding='utf-8') as f_input_csv:
    csv_input = csv.reader(f_input_csv)
    header = next(csv_input)

    for row in csv_input:
        filename, width, height, class_name, xmin, ymin, xmax, ymax = row
        entries_by_filename[filename].append(row) #for whole csv to xml
        #if you want some desire classes then comment above line and uncomment if and else comment
        #if class_name == "yes":  #for more class - if class_name in ["traffic_sign", "car", "bike"]
        #    entries_by_filename[filename].append(row)
        #else:
        #    entries_by_filename[filename].append(row)
        


for filename, entries in entries_by_filename.items():
    write_xml(save_root2, filename, entries)
    
