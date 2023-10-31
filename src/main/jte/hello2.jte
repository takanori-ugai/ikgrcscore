@param com.fujitsu.ikgrcscore.HelloPage page
@param com.fujitsu.ikgrcscore.Localizer localizer

<html lang="en">
<body>
</body>
</html>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="../assets/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">

    <title>Hello, world!</title>
</head>
<body>
<div class="container-fluid mt-3">
        <div class="text-center fs-1 bg-primary text-white">Hello, world!</div>
    <div class="row">
        <div class="col"> ${localizer.localize("hello.visitor")}</div>
        <div class="col">${localizer.localize("hello.user-of-the-day", page.userName, page.userKarma)}</div>
    </div>
    <div class="row">
        <div class="col">
            <form method="post" action="./helloPost">
                <input type="text" name="name" id="name" required>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </div>
    <!-- Content here -->
</div>

<!-- Optional JavaScript; choose one of the two! -->

<!-- Option 1: Bootstrap Bundle with Popper -->
<script src="../assets/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>
</html>