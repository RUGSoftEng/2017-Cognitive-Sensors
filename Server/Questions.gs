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
    object["Start"] = questionData[y][1];
    object["NextQuestion"] = questionData[y][2];
    object['Question'] = questionData[y][3];
    object['QuestionType'] = questionData[y][4];
    if (object['QuestionType'] === 'MC') {
      object['Answers'] = [];
      // Add the multiple choices
      var x = 5;
      while (questionData[y][x] !== undefined && questionData[y][x] !== '') {
        object['Answers'].push(questionData[y][x]);
        x++;
      }
    }
    questions.push(object);
  }
  return HtmlService.createHtmlOutput(JSON.stringify(questions));
}
