<?php
    error_reporting(E_ALL);
    ini_set('display_error', 1);

    include('dbcon.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android) {
        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받습니다.

        $value = $_POST['value'];
        $state = $_POST['state'];

        if(empty($value)) {
            $errMSG = "목표 값을 설정해주세요.";
        } else if(empty($state)) {
            $errMSG = "목표를 설정해주세요.";
        }
        if($state == "1") {
            if(!isset($errMSG)) {
                try {
                    $stmt = $con -> prepare("
                                            UPDATE case1_goal
                                            SET temp = {$value}
                                            WHERE goal = 'goal'
                                            ");

                    if($stmt -> execute()) {
                        $successMSG = "목표를 설정하였습니다.";
                    } else {
                        $errMSG = "목표 설정 에러";
                    }
                } catch(PDOException $e) {
                    die("Database error : ". $e -> getMessage());
                }
            }
        } else if($state == "2") {
            if(!isset($errMSG)) {
                try {
                    $stmt = $con -> prepare("
                                            UPDATE case1_goal
                                            SET water = {$value}
                                            WHERE goal = 'goal'
                                            ");

                    if($stmt -> execute()) {
                        $successMSG = "목표를 설정하였습니다.";
                    } else {
                        $errMSG = "목표 설정 에러";
                    }
                } catch(PDOException $e) {
                    die("Database error : ". $e -> getMessage());
                }
            }
        }
    }
?>

<?php
    if(isset($errMSG)) echo $errMSG;
    if(isset($successMSG)) echo $successMSG;

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if(!$android) {
?>
        <html>
            <body>
                <form action = "<?php $_PHP_SELF ?>" method = "POST">
                
                    value : <input type = "text" name = "value" />
                    state : <input type = "text" name = "state" />
                    <input type = "submit" name = "submit" />
                </form>
            </body>
        </html>
<?php
    }
?>