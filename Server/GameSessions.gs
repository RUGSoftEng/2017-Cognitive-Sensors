// This function accepts an object to add to the database
function doPost(e) {
  // Parse it as JSON
  const data = JSON.parse(e.parameter['data']);
  
  const playerId = parseInt(data['PlayerId'], 10);
  
  if (isNaN(playerId)) {
    Logger.log('Invalid player id: %s', data['PlayerId']);
    return;
  }
  
  // The sheets
  const spreadSheet = SpreadsheetApp.getActiveSpreadsheet();
  
  const gameSessionSheet = spreadSheet.getSheetByName('GameSessions');
  
  // Add all game sessions included in the object
  const gameSessions = data['GameSessions'];
  for (var i = 0; i < gameSessions.length; i++) {
    const gameSession = gameSessions[i];
    
    // Calculate the time
    const gameSessionMs = parseInt(gameSession['Time']);
    if (isNaN(gameSessionMs)) {
      Logger.log('Invalid time (ms): %s', gameSession['Time']);
      return;
    }
    const gameSessionTime = (new Date(gameSessionMs)).toString();
    
    // The game type
    const gameType = gameSession['GameType'];
    if (!checkGameType(gameType)) {
      Logger.log('Invalid game type: %s', gameSession['GameType']);
      return;
    }
    
    const gameSessionData = gameSessionSheet.getDataRange().getValues();
    const previousGameSessionId = parseInt(gameSessionData[gameSessionData.length - 1][0]);
    const gameSessionId = isNaN(previousGameSessionId) ?  1 : previousGameSessionId + 1;
    
    gameSessionSheet.appendRow([gameSessionId, playerId, gameSessionTime, gameType]);
    
    // Read the numberguesses
    const numberGuesses = gameSession['NumberGuesses'];
    for (var j = 0; j < numberGuesses.length; j++)
      if (!parseNumberGuess(numberGuesses[j])) return;
    
    // Read the question answers
    const questionAnswers = gameSession['QuestionAnswers'];
    for (var j = 0; j < questionAnswers.length; j++)
        if (!parseQuestionAnswer(questionAnswers[j])) return;
  }
}

/**
 * This function accepts a game type string and checks its validity
 */
function checkGameType (type) {
  return type === 'NumberGame';
}

/**
 * This function accepts the parsed JSON of a number guess and outputs the converted object. Returns false if the function fails.
 */
function parseNumberGuess(numberGuess, gameSessionId) {
  const spreadSheet = SpreadsheetApp.getActiveSpreadsheet();
  const numberGuessSheet = spreadSheet.getSheetByName('NumberGuesses');
  const numberGuessCells = numberGuessSheet.getDataRange().getValues();
  
  const previousNumberGuessId = parseInt(numberGuessCells[numberGuessCells.length - 1][0]);
  const numberGuessId = isNaN(previousNumberGuessId) ? 1 :  previousNumberGuessId + 1;
  
  const numberGuessMs = parseInt(numberGuess['Time']);
  if (isNaN(numberGuessMs)) {
    Logger.log('Invalid number guess time: %s', numberGuess['Time']);
    return false;
  }
  
  const responseTime = parseFloat(numberGuess['ResponseTime']);
  if (isNaN(responseTime)) {
    Logger.log('Invalid response time for number guess: %s', numberGuess['ResponseTime']);
    return false;
  }
  
  const isGo = (numberGuess['isGo'].toLowerCase() === 'true');
  const correct = (numberGuess['Correct'].toLowerCase() === 'true');
                   
  const number = parseInt(numberGuess['Number']);
  if (isNaN(number)) {
    Logger.log('Invalid number: %s', numberGuess['Number']);
    return false;
  }
  
  numberGuessSheet.appendRow([
    gameSessionId,
    numberGuessId,
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
  
  const questionAnswerMs = parseInt(questionAnswer['Time']);
  if (isNaN(questionAnswerMs)) {
    Logger.log('Invalid question answer time: %s', questionAnswer['Time']);
    return false;
  }
  
  const questionId = parseInt(questionAnswer['QuestionId']);
  if (isNaN(questionId)) {
      Logger.log('Invalid question id: %s' + questionAnswer['QuestionId']);
      return false;
  }
  
  questionAnswerSheet.appendRow([
        gameSessionId,
        (new Date(questionAnswerMs)).toString(),
        questionAnswer['Answer'],
        questionId
      ]);
  return true;
}
