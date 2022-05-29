<?php
    $conn = mysqli_connect("localhost", "root", "432200", "db_cityfarm");
    mysqli_set_charset($conn, "utf8");
    $res = mysqli_query($conn, "select * from case2 ORDER BY date DESC");
    $result = array();
    while($row = mysqli_fetch_array($res)) {
        array_push($result, array('date'=>$row[0], 'temp'=>$row[1], 'moisture'=>$row[2], 'illuminance'=>$row[3], 'water'=>$row[4]));
    }
    echo json_encode(array("result"=>$result));
    mysqli_close($conn);
?>