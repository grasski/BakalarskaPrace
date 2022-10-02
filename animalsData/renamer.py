# Python 3 code to rename multiple
# files in a directory or folder
 
# importing os module
import os
 
# Function to rename multiple files
def main():

    folder = os.path.join("images", "Capybara")
    for count, filename in enumerate(os.listdir(folder)):
        source = os.path.join(folder, filename)
        newName = os.path.join(folder, f"Capybara_{count}.jpg")

        os.rename(source, newName)
 
# Driver Code
if __name__ == '__main__':
    main()
    print("done")
