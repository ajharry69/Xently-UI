# Contribution Guidelines

**All contributions are welcome!** But before it accepted, its better done in a team-friendly
way by following a list of standard guidelines as listed below.

## Cloning and github pushing guideline

1. Folk the original repository.
2. Clone the forked repo(your copy).
3. Add [original repo][url-original-repo] as a **remote** called `remote-name`. This `remote-name` will be used to pull changes from the [original repo][url-original-repo]'s **master** branch

    > [url-original-repo][url-original-repo]
    >
    > ```git
    > git remote add <remote-name> [url-original-repo] # Add remote
    > git pull <remote-name> master # Update your local copy with changes from original repo master branch
    > ```

4. Branch from your local copy's **master branch**(cloned from your account) by

    > ```git
    > git branch <branch-name>
    > git checkout <branch-name>
    > ```

5. Make your changes, comment the code (following the code-style described below).
6. Commit the changes.
7. Move back to the **master branch** by
    > `git checkout master`

8. **Rebase** / **merge** your `branch-name`(created at **4**) commits by
    > **NOTE:** Pull changes from the [original repo][url-original-repo] using guideline 3's,
    > 2nd command before executing the command below
    >
    > `git rebase|merge <branch-name>`

9. **Push** your changes to your(copy's) **master branch** or which ever branch you want to push the changes to.
    > `git push -u origin master`

10. Create a **pull request** to the **original copy** for the branch you pushed changes to at 9
11. **All done!**

[url-original-repo]: https://github.com/ajharry69/xently-dialog.git

## Code writing guidelines

1. Comment you codes
  
#### Remarks

"None of us is as smart as ALL of us"!

**Happy coding!**
