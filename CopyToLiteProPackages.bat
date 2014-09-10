echo Y | DEL "..\Vulcan-Tech-Gospel-Lite\assets\*.*"
echo Y | DEL "..\Vulcan-Tech-Gospel-Pro\assets\*.*"

echo Y | DEL "..\Vulcan-Tech-Gospel-Lite\assets\iconView\*.*"
echo Y | DEL "..\Vulcan-Tech-Gospel-Pro\assets\iconView\*.*"

echo Y | DEL "..\Vulcan-Tech-Gospel-Lite\res\drawable-hdpi\*.*"
echo Y | DEL "..\Vulcan-Tech-Gospel-Lite\res\drawable-ldpi\*.*"
echo Y | DEL "..\Vulcan-Tech-Gospel-Lite\res\drawable-mdpi\*.*"
echo Y | DEL "..\Vulcan-Tech-Gospel-Lite\res\drawable-xhdpi\*.*"

echo Y | DEL "..\Vulcan-Tech-Gospel-Pro\res\drawable-hdpi\*.*"
echo Y | DEL "..\Vulcan-Tech-Gospel-Pro\res\drawable-ldpi\*.*"
echo Y | DEL "..\Vulcan-Tech-Gospel-Pro\res\drawable-mdpi\*.*"
echo Y | DEL "..\Vulcan-Tech-Gospel-Pro\res\drawable-xhdpi\*.*"

xcopy /Y .gitignore "..\Vulcan-Tech-Gospel-Lite\.gitignore"
xcopy /Y .gitignore "..\Vulcan-Tech-Gospel-Pro\.gitignore"

xcopy /s /Y assets "..\Vulcan-Tech-Gospel-Lite\assets"
xcopy /s /Y assets "..\Vulcan-Tech-Gospel-Pro\assets"

xcopy /s /Y res\drawable-hdpi "..\Vulcan-Tech-Gospel-Lite\res\drawable-hdpi"
xcopy /s /Y res\drawable-ldpi "..\Vulcan-Tech-Gospel-Lite\res\drawable-ldpi"
xcopy /s /Y res\drawable-mdpi "..\Vulcan-Tech-Gospel-Lite\res\drawable-mdpi"
xcopy /s /Y res\drawable-xhdpi "..\Vulcan-Tech-Gospel-Lite\res\drawable-xhdpi"

xcopy /s /Y res\drawable-hdpi "..\Vulcan-Tech-Gospel-Pro\res\drawable-hdpi"
xcopy /s /Y res\drawable-ldpi "..\Vulcan-Tech-Gospel-Pro\res\drawable-ldpi"
xcopy /s /Y res\drawable-mdpi "..\Vulcan-Tech-Gospel-Pro\res\drawable-mdpi"
xcopy /s /Y res\drawable-xhdpi "..\Vulcan-Tech-Gospel-Pro\res\drawable-xhdpi"