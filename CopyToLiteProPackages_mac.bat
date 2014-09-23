echo Y | rm -Rf "../Vulcan-Tech-Gospel-Lite/assets/"
echo Y | rm -Rf "../Vulcan-Tech-Gospel-Pro/assets/"

echo Y | rm -Rf "../Vulcan-Tech-Gospel-Lite/assets/iconView/"
echo Y | rm -Rf "../Vulcan-Tech-Gospel-Pro/assets/iconView/"

echo Y | rm -Rf "../Vulcan-Tech-Gospel-Lite/res/drawable-hdpi/"
echo Y | rm -Rf "../Vulcan-Tech-Gospel-Lite/res/drawable-ldpi/"
echo Y | rm -Rf "../Vulcan-Tech-Gospel-Lite/res/drawable-mdpi/"
echo Y | rm -Rf "../Vulcan-Tech-Gospel-Lite/res/drawable-xhdpi/"

echo Y | rm -Rf "../Vulcan-Tech-Gospel-Pro/res/drawable-hdpi/"
echo Y | rm -Rf "../Vulcan-Tech-Gospel-Pro/res/drawable-ldpi/"
echo Y | rm -Rf "../Vulcan-Tech-Gospel-Pro/res/drawable-mdpi/"
echo Y | rm -Rf "../Vulcan-Tech-Gospel-Pro/res/drawable-xhdpi/"

cp -f .gitignore "../Vulcan-Tech-Gospel-Lite/.gitignore"
cp -f .gitignore "../Vulcan-Tech-Gospel-Pro/.gitignore"

cp -f -R assets "../Vulcan-Tech-Gospel-Lite/assets"
cp -f -R assets "../Vulcan-Tech-Gospel-Pro/assets"

cp -f -R res/drawable-hdpi "../Vulcan-Tech-Gospel-Lite/res/drawable-hdpi"
cp -f -R res/drawable-ldpi "../Vulcan-Tech-Gospel-Lite/res/drawable-ldpi"
cp -f -R res/drawable-mdpi "../Vulcan-Tech-Gospel-Lite/res/drawable-mdpi"
cp -f -R res/drawable-xhdpi "../Vulcan-Tech-Gospel-Lite/res/drawable-xhdpi"

cp -f -R res/drawable-hdpi "../Vulcan-Tech-Gospel-Pro/res/drawable-hdpi"
cp -f -R res/drawable-ldpi "../Vulcan-Tech-Gospel-Pro/res/drawable-ldpi"
cp -f -R res/drawable-mdpi "../Vulcan-Tech-Gospel-Pro/res/drawable-mdpi"
cp -f -R res/drawable-xhdpi "../Vulcan-Tech-Gospel-Pro/res/drawable-xhdpi"