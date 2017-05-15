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
    object["start"] = questionData[y][1];
    object["nextQuestion"] = questionData[y][2];
    object['question'] = questionData[y][3];
    object['questionType'] = questionData[y][4];
    if (object['questionType'] === 'MC') {
      object['answers'] = [];
      // Add the multiple choices
      var x = 5;
      while (questionData[y][x] !== undefined && questionData[y][x] !== '') {
        object['answers'].push(questionData[y][x]);
        x++;
      }
    }
    questions.push(object);
  }
  return ContentService.createTextOutput(JSON.stringify(questions))
    .setMimeType(ContentService.MimeType.JSON);
}
