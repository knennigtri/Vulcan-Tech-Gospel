Set fso = CreateObject("Scripting.FileSystemObject")

set oFldr = fso.getfolder("D:\My Documents\workspace\Vulcan Tech Gospel\raw data\poi_patterns")

Dim str
Dim sp
for each ofile in oFldr.Files
  sp = Split(ofile.Name)
  str = sp(0) + "_poi.mp4"
  ofile.Name = str
Next

MsgBox("DONE.")