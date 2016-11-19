require "time"
require "date"
#re = /([0-9].[0-9][0-9][0-9])\s(-[0-9].[0-9][0-9][0-9])\s([0-9].[0-9][0-9][0-9])\s([0-9][0-9][0-9])/
re = /([0-9].[0-9][0-9][0-9])\s([-]?[0-9].[0-9][0-9][0-9])\s([0-9].[0-9][0-9][0-9])\s([0-9][0-9][0-9])/

#file = "usa3_fin.txt"
file = "nomal_fin.txt"
mode = "a"

sumDif = 0

xSum = 0
ySum = 0
zSum = 0

xAve = 0
yAve = 0
zAve = 0

count = 0

ARGF.each_line do |line|
  if re.match(line)
    xValue = $1.to_f
    p xValue
    yValue = $2.to_f
    p yValue
    zValue = $3.to_f
    p zValue
    timeDif = $4.to_f
    
    sumDif += 1
    xSum = xSum + xValue
    ySum = ySum + yValue
    zSum = zSum + zValue
    count += 1
    if sumDif == 4 
      xAve = xSum.quo(count)
      yAve = ySum.quo(count)
      zAve = zSum.quo(count)
      str = "#{xAve} #{yAve} #{zAve}\n"
      #ファイルに書き出し
        open(file , mode ){|f| f.write(str)}
        xAve = 0
        yAve = 0
        zAve = 0
        xSum = 0
        ySum = 0
        zSum = 0
        count = 0
        sumDif = 0
    end
end
end
