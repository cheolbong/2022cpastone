<?php
    $conn = mysqli_connect("localhost", "root", "432200", "db_cityfarm");
    mysqli_set_charset($conn, "utf8");
    $res = mysqli_query($conn, "select * from case1_goal");
    $result = array();
    while($row = mysqli_fetch_array($res)) {
        array_push($result, array('goal'=>$row[0], 'temp'=>$row[1], 'water'=>$row[2]));
    }
    echo json_encode(array("result"=>$result));
    mysqli_close($conn);
?>