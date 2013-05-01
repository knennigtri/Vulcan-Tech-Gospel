echo Y | DEL "D:\My Documents\workspace\Vulcan Tech Gospel Lite\assets\*.*"
echo Y | DEL "D:\My Documents\workspace\Vulcan Tech Gospel Pro\assets\*.*"

echo Y | DEL "D:\My Documents\workspace\Vulcan Tech Gospel Lite\assets\iconView\*.*"
echo Y | DEL "D:\My Documents\workspace\Vulcan Tech Gospel Pro\assets\iconView\*.*"

echo Y | DEL "D:\My Documents\workspace\Vulcan Tech Gospel Lite\res\drawable-hdpi\*.*"
echo Y | DEL "D:\My Documents\workspace\Vulcan Tech Gospel Lite\res\drawable-ldpi\*.*"
echo Y | DEL "D:\My Documents\workspace\Vulcan Tech Gospel Lite\res\drawable-mdpi\*.*"
echo Y | DEL "D:\My Documents\workspace\Vulcan Tech Gospel Lite\res\drawable-xhdpi\*.*"

echo Y | DEL "D:\My Documents\workspace\Vulcan Tech Gospel Pro\res\drawable-hdpi\*.*"
echo Y | DEL "D:\My Documents\workspace\Vulcan Tech Gospel Pro\res\drawable-ldpi\*.*"
echo Y | DEL "D:\My Documents\workspace\Vulcan Tech Gospel Pro\res\drawable-mdpi\*.*"
echo Y | DEL "D:\My Documents\workspace\Vulcan Tech Gospel Pro\res\drawable-xhdpi\*.*"


xcopy /s /Y assets "D:\My Documents\workspace\Vulcan Tech Gospel Lite\assets"
xcopy /s /Y assets "D:\My Documents\workspace\Vulcan Tech Gospel Pro\assets"

xcopy /s /Y res\drawable-hdpi "D:\My Documents\workspace\Vulcan Tech Gospel Lite\res\drawable-hdpi"
xcopy /s /Y res\drawable-ldpi "D:\My Documents\workspace\Vulcan Tech Gospel Lite\res\drawable-ldpi"
xcopy /s /Y res\drawable-mdpi "D:\My Documents\workspace\Vulcan Tech Gospel Lite\res\drawable-mdpi"
xcopy /s /Y res\drawable-xhdpi "D:\My Documents\workspace\Vulcan Tech Gospel Lite\res\drawable-xhdpi"

xcopy /s /Y res\drawable-hdpi "D:\My Documents\workspace\Vulcan Tech Gospel Pro\res\drawable-hdpi"
xcopy /s /Y res\drawable-ldpi "D:\My Documents\workspace\Vulcan Tech Gospel Pro\res\drawable-ldpi"
xcopy /s /Y res\drawable-mdpi "D:\My Documents\workspace\Vulcan Tech Gospel Pro\res\drawable-mdpi"
xcopy /s /Y res\drawable-xhdpi "D:\My Documents\workspace\Vulcan Tech Gospel Pro\res\drawable-xhdpi"