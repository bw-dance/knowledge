@echo off
echo "DOCS PUSH BAT"

echo "1. Move to working directory" 
cd /d G:\ComputerStudy\cloudnote\knowledge\knowledges

echo "2. Start submitting code to the local repository"
git add .

echo "3. Commit the changes to the local repository"
git commit -m "save note"

echo "4. Push the changes to the remote git server"
git push gitee HEAD:develop
git push github HEAD:develop
 
echo "Batch execution complete!"
pause
