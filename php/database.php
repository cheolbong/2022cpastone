<?php
    $conn = mysqli_connect("localhost", "root", "432200", "db_cityfarm");

    if($conn) {
        echo "DB연동 성공";
    } else {
        echo "DB연동 실패";
    }
?>