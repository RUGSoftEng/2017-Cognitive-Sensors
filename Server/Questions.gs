// An empty GET request will fetch the questions.
function doGet(e) {
  // Load the quetions from the sheet
  const spreadSheet = SpreadsheetApp.getActiveSpreadsheet();
  const questionData = spreadSheet.getSheetByName('Questions').getDataRange().getValues();
  
  // This object will be converted to JSON and sent
  const questions = [];
  
  // Add the questions one by one
  for (var y = 1; y < questionData.length; y++) {
    var object = {};
    object["questionID"] = questionData[y][0];
    object["start"] = questionData[y][1];
    object['question'] = questionData[y][3];
    object['questionType'] = questionData[y][4];
    if (object['questionType'] === 'MC') {
      object['answers'] = [];
      object['nextQuestion'] = [];
      object['onOffTask'] = [];
      // Add the multiple choices
      do {
        object['answers'].push(questionData[y][5]);
        object['nextQuestion'].push(parseInt(questionData[y][2]));
        object['onOffTask'].push(parseInt(questionData[y][6]));
        y++;
        
        // If the next line is empty, there is another MC option
      } while (questionData[y + 1][0] === undefined || questionData[y + 1][0] === '');
    } else {
      object['nextQuestion'] = [parseInt(questionData[y][2])];
      object['onOffTask'] = [parseInt(questionData[y][6])];
    }
    questions.push(object);
  }
  return ContentService.createTextOutput(JSON.stringify(questions))
    .setMimeType(ContentService.MimeType.JSON);
}
