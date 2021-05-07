<?php

require("connect.php");
	
	if($_SERVER['REQUEST_METHOD'] == 'POST') {

		//$id = $_POST["id"];
		$userid = $_POST["userid"];

	$sqlQuery = "SELECT * FROM imagestore WHERE userid = '$userid'";

	$result = mysqli_query($conn, $sqlQuery);
	$row = mysqli_affected_rows($conn);

	$imageDetails = NULL;

	if($row > 0){
	$response["success"] = 1;
    $response["message"] = "Data Fetched.";
    $response["data"] = array();

	while($row = mysqli_fetch_array($result)){

		$imageDetails["id"] = $row[0];

		$imageDetails["userid"] = $row[1];

     	$imageLocation = $row[2];

		$imageFile = file_get_contents($imageLocation);

		$imageDetails["image_location"] = base64_encode($imageFile);

		 array_push($response["data"], $imageDetails);
	}
}
else{
    $response["success"] = 0;
    $response["message"] = "Data not Fetch.";
}
	
	echo json_encode($response);
	mysqli_close($conn);

}

?>