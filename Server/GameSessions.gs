function testDoPost() {
  json = '{ "gameSessions":[ { "gameSessionId":11, "gameType":"numberGame", "numberGuesses":[ { "correct":false, "isGo":true, "number":0, "responseTime":380556395, "time":0 }, { "correct":false, "isGo":true, "number":0, "responseTime":380687985, "time":0 }, { "correct":false, "isGo":true, "number":0, "responseTime":380744687, "time":0 }, { "correct":false, "isGo":true, "number":0, "responseTime":380805490, "time":0 }, { "correct":false, "isGo":true, "number":0, "responseTime":380887813, "time":0 }, { "correct":false, "isGo":true, "number":0, "responseTime":381280626, "time":0 }, { "correct":false, "isGo":true, "number":0, "responseTime":382257666, "time":0 }, { "correct":false, "isGo":true, "number":9, "responseTime":766, "time":1490802832304 }, { "correct":false, "isGo":true, "number":9, "responseTime":766, "time":1490802832304 }, { "correct":false, "isGo":true, "number":2, "responseTime":153, "time":1490802951090 }, { "correct":false, "isGo":true, "number":2, "responseTime":153, "time":1490802951090 }, { "correct":false, "isGo":true, "number":2, "responseTime":153, "time":1490802951090 }, { "correct":false, "isGo":true, "number":7, "responseTime":745, "time":1490804119961 }, { "correct":false, "isGo":true, "number":7, "responseTime":745, "time":1490804119961 }, { "correct":false, "isGo":true, "number":8, "responseTime":163, "time":1490804428627 }, { "correct":false, "isGo":true, "number":8, "responseTime":163, "time":1490804428627 }, { "correct":false, "isGo":true, "number":8, "responseTime":163, "time":1490804428627 }, { "correct":false, "isGo":true, "number":8, "responseTime":163, "time":1490804428627 } ], "questionAnswers":[], "time":1490804428627 } ], "playerId":-1 }';
  e = {parameter: {data: json }};
  doPost(e);
}

// This function accepts an object to add to the database
function doPost(e) {
  // Parse it as JSON
  const data = JSON.parse(e.parameter['data']);
  
  // Parse the player id
  const playerId = parseInt(data['playerId'], 10);
  
  if (isNaN(playerId)) {
    Logger.log('Invalid player id: %s', data['playerId']);
    return;
  }
  
  // The sheets
  const spreadSheet = SpreadsheetApp.getActiveSpreadsheet();
  const gameSessionSheet = spreadSheet.getSheetByName('GameSessions');
    
  // Calculate the new game session id
  const gameSessionData = gameSessionSheet.getDataRange().getValues();
  const previousGameSessionId = parseInt(gameSessionData[gameSessionData.length - 1][0]);
  var gameSessionId = isNaN(previousGameSessionId) ?  1 : previousGameSessionId + 1;
  
  // Calculate the new number guess id
  const numberGuessSheet = spreadSheet.getSheetByName('numberGuesses');
  const numberGuessCells = numberGuessSheet.getDataRange().getValues();
  const previousNumberGuessId = parseInt(numberGuessCells[numberGuessCells.length - 1][1]);
  var numberGuessId = isNaN(previousNumberGuessId) ? 0 : previousNumberGuessId;
  
  // Add all game sessions included in the object
  const gameSessions = data['gameSessions'];
  for (var i = 0; i < gameSessions.length; i++) {
    const gameSession = gameSessions[i];
    
    // Calculate the time
    const gameSessionMs = parseInt(gameSession['time']);
    if (isNaN(gameSessionMs)) {
      Logger.log('Invalid time (ms): %s', gameSession['time']);
      return;
    }
    const gameSessionTime = (new Date(gameSessionMs)).toString();
    
    // The game type
    const gameType = gameSession['gameType'];
    if (!checkGameType(gameType)) {
      Logger.log('Invalid game type: %s', gameSession['gameType']);
      return;
    }
    
    gameSessionSheet.appendRow([gameSessionId + i, playerId, gameSessionMs, gameSessionTime, gameType]);
    
    // Read the numberguesses
    const numberGuesses = gameSession['numberGuesses'];
    for (var j = 0; j < numberGuesses.length; j++) {
      numberGuessId++;
      if (!parseNumberGuess(numberGuesses[j], gameSessionId + i, numberGuessId)) return;
    }
    
    // Read the question answers
    const questionAnswers = gameSession['questionAnswers'];
    for (var j = 0; j < questionAnswers.length; j++)
        if (!parseQuestionAnswer(questionAnswers[j], gameSessionId + i)) return;
  }
}

/**
 * This function accepts a game type string and checks its validity
 */
function checkGameType (type) {
  return type === 'numberGame';
}

/**
 * This function accepts the parsed JSON of a number guess and outputs the converted object. Returns false if the function fails.
 */
function parseNumberGuess(numberGuess, gameSessionId, numberGuessId) {
  const spreadSheet = SpreadsheetApp.getActiveSpreadsheet();
  const numberGuessSheet = spreadSheet.getSheetByName('numberGuesses');
  const numberGuessCells = numberGuessSheet.getDataRange().getValues();
  
  const numberGuessMs = parseInt(numberGuess['time']);
  if (isNaN(numberGuessMs)) {
    Logger.log('Invalid number guess time: %s', numberGuess['time']);
    return false;
  }
  
  const responseTime = parseFloat(numberGuess['responseTime']);
  if (isNaN(responseTime)) {
    Logger.log('Invalid response time for number guess: %s', numberGuess['responseTime']);
    return false;
  }
  
  const isGo = numberGuess['isGo'];
  const correct = numberGuess['correct'];
                   
  const number = parseInt(numberGuess['number']);
  if (isNaN(number)) {
    Logger.log('Invalid number: %s', numberGuess['number']);
    return false;
  }
  
  numberGuessSheet.appendRow([
    gameSessionId,
    numberGuessId,
    numberGuessMs,
    (new Date(numberGuessMs)).toString(),
    responseTime,
    isGo,
    correct,
    number
  ]);
  
  return true;
}

function parseQuestionAnswer(questionAnswer, gameSessionId) {
  const spreadSheet = SpreadsheetApp.getActiveSpreadsheet();
  const questionAnswerSheet = spreadSheet.getSheetByName('QuestionAnswers');
  
  const questionAnswerMs = parseInt(questionAnswer['time']);
  if (isNaN(questionAnswerMs)) {
    Logger.log('Invalid question answer time: %s', questionAnswer['time']);
    return false;
  }
  
  const questionId = parseInt(questionAnswer['questionId']);
  if (isNaN(questionId)) {
      Logger.log('Invalid question id: %s' + questionAnswer['questionId']);
      return false;
  }
  
  questionAnswerSheet.appendRow([
        gameSessionId,
        questionAnswerMs,
        (new Date(questionAnswerMs)).toString(),
        questionAnswer['answer'],
        questionId
      ]);
  
  return true;
}
