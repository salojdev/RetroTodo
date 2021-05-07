<?php
require("connect.php");

$response = array();
//alamat->email,telepon->mobile
if($_SERVER["REQUEST_METHOD"] == "POST"){

      $name = $_POST["name"];
    $username = $_POST["username"];
    $mobileno = $_POST["mobileno"];
     $password = $_POST["password"];

 $password = password_hash($password, PASSWORD_DEFAULT);

      $query = "INSERT INTO users(name,username,mobileno,password) VALUES ('$name', '$username', '$mobileno','$password')";
    $sql = mysqli_query($conn, $query);
   // $row = mysqli_affected_rows($conn);
         $last_id = $conn->insert_id;


//echo $last_id; die();

         
 $response['success'] = 1;
  $response['message'] = "Successfully Register.";
    $response['data'] = array();

    if($last_id > 0){

            $index['userid'] = $last_id;
            $index['username'] = $username;  
         
            array_push($response['data'], $index);
     //   $response["success"] = 1;
      //  $response["message"] = "Data inserted.";
    
    }
    else{
        $response["success"] = 0;
        $response["message"] = "Data not insert.";
    }
}
else{
    $response["success"] = 0;
    $response["message"] = "Please fill data.";
}

echo json_encode($response);
mysqli_close($conn);

?>