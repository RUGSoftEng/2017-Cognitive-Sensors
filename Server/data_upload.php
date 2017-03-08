<?php
$servername = 'localhost';
$username = "username"
$pass = 'Pasword';
$dbname = "myDB";
$gamesession 'GameSession';
$numberGuess = 'NumberGuess';
$questionAnswer = 'QuestionAnswer';
$tablename= $_GET["tablename"];

// gamesession
$playerID = $_GET["playerID"];
$gameType = $_GET["GameType"];
$time = $_GET["Time"];



// questionAnswer


$question='';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: ". $conn->connect_error);
} 

if($tablename == 'GameSession')
{
	$sql = "INSERT INTO ".$tablename."(ID,PlayerID,GameType,Time)
	VALUES ('',".$playerID.",".$gameType.",".$time.")";
	if ($conn->query($sql) === TRUE) {
		echo "New record created successfully";
	} else {
		echo "Error: " . $sql . "<br>" . $conn->error;
	}
}else if($tablename=='NumberGuess')
{
	// numberGuess
	$responsetime = $_GET["ResponseTime"];
	$correct = $_GET["Correct"];
	$num_gamesession;
	$sql = "SELECT ID FROM GameSession WHERE PlayerID =".$playerID.",GameType = ".$gameType.",Time = ".$time;
	$result = $conn->query($sql);
	$$num_gamesession; = $result;
	$sql = "INSERT INTO ".$tablename."(ID,ResponseTime,Correct,NumberGameSessions)
	VALUES ('',".$responsetime.",".$correct.",".$num_gamesession.")";
}
else if(tablename == QuestionAnswer)
{
	$gameSessionID;
	$questionID;
	$answer $_GET['Answer'];
	$question = $_GET['question'];
	$sql = "SELECT ID FROM GameSession WHERE PlayerID =".$playerID.",GameType = ".$gameType.",Time = ".$time;
	$result = $conn->query($sql);
	$gameSessionID = $result;
	$sql = "SELECT ID FROM Question WHERE QuestionString ='".$question."'";
	$result = $conn->query($sql);
	$questionID = $result;
	$sql = "INSERT INTO ".$tablename."(ID,GameSessionID,QuestionID,Answer)
	VALUES ('',".$gameSessionID.",".$questionID.",'".$answer."')";
}

?>