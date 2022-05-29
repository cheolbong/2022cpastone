<?php
    include_once("./database.php");
    $temp_goal = $_POST["temp"];
    $water_goal = $_POST["water"];
    $case = $_POST["case"];
    $sql="
        UPDATE {$case}_goal
        SET temp = {$temp}, water = {$water}
        WHERE goal = 'goal'
        ";
    mysqli_query($conn, $sql);
    mysqli_close($conn);
?>