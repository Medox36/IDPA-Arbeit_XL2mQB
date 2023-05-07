Set Shell = CreateObject("WScript.Shell")
desktopPath = Shell.SpecialFolders("Desktop")
Set link = Shell.CreateShortcut(desktopPath + "\XL2mQB.lnk")
link.Description = "Moodle Question Converter"
link.TargetPath = "%localappdata%\XL2mQB\XL2mQB.exe"
link.Save
