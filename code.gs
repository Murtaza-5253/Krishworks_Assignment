var ss = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1dtzppH08Y1zqxQZE9T3bk8Bt-aSEmT_-J8Rf9ykZLPI/edit#gid=0");
var sheet = ss.getSheetByName("Sheet1");

function doPost(e){
  var action = e.parameter.action;
  if(action=='addStudent'){
    return addItem(e);
  }
  if(action=='fetchStudent'){
    return doTriggr();
  }
  if(action=='doTrigger'){
    
    return doTriggr();
  }
  if(action=='doResult'){
    return addResult(e)
  }
}

function doTriggr()
{
  var result = ss.getSheetByName("Result");
  

    if (result != null) {
        ss.deleteSheet(result);
    }

    result = ss.insertSheet();
    result.setName("Result");
    var source = sheet.getRange(1,1,12,3);
    var dest = result.getRange(result.getLastRow()+1,1,12,3);  
    source.copyTo(dest);
    var active = ss.getActiveSheet();
    active.insertColumnAfter(3).getRange(1,4).setValue("Result");
  var records={};
  var rows = 
  sheet.getRange(2,1,sheet.getLastRow()-1,sheet.getLastColumn()).getValues();
  data=[];
  for(var r=0, l=rows.length; r<l; r++){
    var row = rows[r],
    record = {};
    record['Student_Id']=row[0];
    record['Student_Name']=row[1];
    record['Marks']=row[2];
    data.push(record);
  }
  records.items=data;
  var result=JSON.stringify(records);
  return ContentService.createTextOutput(result).setMimeType(ContentService.MimeType.JSON);
  //accItem();
}


function addItem(e){
  var id = e.parameter.Student_ID;
  var name = e.parameter.Student_Name;
  var marks = e.parameter.Marks;

  sheet.appendRow([id,name,marks]);
  return ContentService.createTextOutput("Success").setMimeType(ContentService.MimeType.TEXT);
}


function addResult(e)
{
  var resultsheet = ss.getSheetByName("Result");
  var result=[[e.parameter.MResult]];
  //result = e.parameter.MResult;
  Logger.log(result);


 
    var newarr = [[],[]];
    newarr[0].push(result);  

    var out = [],temparr=[],i=0;
    for(i=0;i<newarr.length;i++){
      temparr=[];
      temparr.push(newarr[i]);
      out.push(temparr);
    }
    
    var range = resultsheet.getRange(2,4,newarr.length,1);
    range.setValues(out);


  

  

  return ContentService.createTextOutput(newarr).setMimeType(ContentService.MimeType.TEXT).append(newarr);
  
}


function accItem(){
  var records={};
  var rows = 
  sheet.getRange(2,1,sheet.getLastRow()-1,sheet.getLastColumn()).getValues();
  data=[];
  for(var r=0, l=rows.length; r<l; r++){
    var row = rows[r],
    record = {};
    record['Student_Id']=row[0];
    record['Student_Name']=row[1];
    record['Marks']=row[2];
    data.push(record);
  }
  records.items=data;
  var result=JSON.stringify(records);
  //Logger.log(result);
  return ContentService.createTextOutput(result).setMimeType(ContentService.MimeType.JSON);
}