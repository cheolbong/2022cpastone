<?php
    include_once("./database.php");
    //입력 데이터 설정
    $case = $_POST["case"];
    $temp = $_POST["temp"];
    $moisture = $_POST["moisture"];
    $illuminance = $_POST["illuminance"];
    $water = $_POST["water"];
    $sql="
        INSERT INTO {$case} VALUES(
        now(),
        {$temp},
        {$moisture},
        {$illuminance},
        {$water}
        )";
    mysqli_query($conn, $sql);
    mysqli_close($conn);
?>