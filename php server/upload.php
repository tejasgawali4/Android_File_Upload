<?php
 
$con=mysqli_connect("localhost","root","","test");
// Path to move uploaded files
$target_path = dirname(__FILE__).'/uploads_files/';
  
if (isset($_FILES['file']['name'])) 
{
    $target_path = $target_path . basename($_FILES['file']['name']);

    $path = basename($_FILES['file']['name']);
 
    try {
        // Throws exception incase file is not being moved
        if (!move_uploaded_file($_FILES['file']['tmp_name'], $target_path)) {
            // make error flag true
            echo json_encode(array('status'=>'fail', 'message'=>'could not move file'));
        }
        else 
        {
            $query = "INSERT INTO `file_upload`(`path`,`name`) VALUES ('$target_path','$path')";
                
            if (mysqli_query($con,$query)) 
            {
                //File successfully uploaded
                echo json_encode(array('status'=>'success', 'message'=>'File Uploaded'));
            }

        }
    } catch (Exception $e) {
        // Exception occurred. Make error flag true
        echo json_encode(array('status'=>'fail', 'message'=>$e->getMessage()));
    }
} else {
    // File parameter is missing
    echo json_encode(array('status'=>'fail', 'message'=>'Not received any file'));
}

?>