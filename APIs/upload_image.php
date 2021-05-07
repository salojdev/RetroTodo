<?php

	include 'connect.php';

if($_SERVER["REQUEST_METHOD"] == "POST") {
	
       	$userid = $_REQUEST["userid"];
	 $file  =  $_FILES["image_location"]["tmp_name"];
	
        $photo="";
	$target_dir = "profile_images/";
	if(!file_exists($target_dir))
	mkdir("$target_dir");

		$photo = 'image_userid_'.$userid.'.jpg';


$sqlQuery = "INSERT INTO `imagestore`(`userid`, `image_location`) VALUES ('$userid', '$target_dir$photo')";

                        if(mysqli_query($conn,$sqlQuery)){
 
	if(isset($_FILES["image_location"],$_REQUEST["userid"])){

		//$photo = 'image'.date('YmdHis').rand().'.jpg';

               if(move_uploaded_file($_FILES["image_location"]["tmp_name"],$target_dir.$photo)){
                   
                                              

	                        	$result["success"] = 1;

	                         	$result["message"] = "Image Uploaded Successfully";
	                         	
                    

                }else{

	                 	$result["success"] = 0;

	                   	$result["message"] = "Image Uploading Failed";
                 }
                 

              }
        echo json_encode($result);
        mysqli_close($conn);
                        }
                        
}
?>