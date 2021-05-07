<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $username = $_POST['username'];
    $password = $_POST['password'];

 require_once 'connect.php';
    $sql = "SELECT * FROM users WHERE username ='$username' ";

    $response = mysqli_query($conn, $sql);

    $result = array();

            $result['success'] = 1;
            $result['message'] = "Successfully login.";
    $result['data'] = array();
    
    if ( mysqli_num_rows($response) == 1 ) {
        
        $row = mysqli_fetch_assoc($response);

        if ( password_verify($password, $row['password']) ) {
            
             $index['userid'] = $row['userid'];
            $index['mobileno'] = $row['mobileno'];
            $index['username'] = $row['username'];
                     

            array_push($result['data'], $index);

            echo json_encode($result);

            mysqli_close($conn);

        } else {

            $result['success'] = "0";
            $result['message'] = "Authentication failed.";
            echo json_encode($result);

            mysqli_close($conn);

        }

    }

}
?>