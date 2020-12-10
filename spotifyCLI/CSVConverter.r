
#https://www.tutorialspoint.com/r/r_json_files.htm
#https://stackoverflow.com/questions/2061897/parse-json-with-r

library("jsonlite")

result <- fromJSON(txt="/app/out/output.json")
json_data_frame <- as.data.frame(result)
write.csv(json_data_frame,"/app/out/spotifyCLI_output.csv", row.names = FALSE)
print(json_data_frame)