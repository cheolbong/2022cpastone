<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>list</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.25/css/jquery.dataTables.css">
        <script src="https://code.jquery.com/jquery-3.6.0.js"integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk="crossorigin="anonymous"></script>
        <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.25/js/jquery.dataTables.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
    </head>
    <style>
        #content {width: 100%; height: 100%; min-width: 1000px;}
        #title_box {text-align: center;}
    </style>
    <?php
        $case = $_GET["case"];
        include_once("./database.php");
        $sql = "SELECT * FROM {$case} ORDER BY date DESC";
        $data = mysqli_query($conn, $sql);
    ?>
    <body>
        <div id="content" class="container">
            <div id="title_box">
                <h3>알림 리스트</h3>
            </div>
            <table id="data_table" class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>시간</th>
                        <th>온도</th>
                        <th>습도</th>
                        <th>조도</th>
                        <th>토양 수분</th>
                    </tr>
                </thead>
                <tbody>
                    <?php
                        while($list = mysqli_fetch_array($data)) {
                            echo "<tr>";
                            echo "<td>".$list["date"]."</td>";
                            echo "<td>".$list["temp"]."</td>";
                            echo "<td>".$list["moisture"]."</td>";
                            echo "<td>".$list["illuminance"]."</td>";
                            echo "<td>".$list["water"]."</td>";
                        }
                    ?>
                </tbody>
            </table>
        </div>
    </body>
</html>
<script>
    $(document).ready(function() {
        $('#data_table').DataTable({
            lengthChange : false,
            searching : false,
            ordering : false,
            info : false,
            order : [0, "desc"],
            displaylength : 50
        });
    });
</script>