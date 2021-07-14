require: slotfilling/slotFilling.sc
  module = sys.zb-common
theme: /

    state: Start
        q!: $regex</start>
        a: Начнём. Напиши "help" для помощи
        script:
            $session.date = {}
            $session.year = {}


    state: Help
        q!: help
        a: 1) "назвать дату" - заполнение слота даты
        a: 2) "печать" - вывод значения даты
        a: 3) "год" - вывод значения года из даты
        a: 4) userID
        
    state: Приветствие
        intent!: /привет
        a: Привет привет

    state: https
        q!: https
        a: http_2 compleate 
        script:
            var url = 'http://content.sberdevices.ru/smartmarket-smide-prod/68305/68306/TOeha4A6hsCK525M.png'
            var response = $http.query(url, {method: "GET", timeout: 600});
            if (response.isOk){
            $session.answer = response
            }else{
            $session.answer = response.error
            }
            $reactions.answer($session.answer);

        
    state: Имя
        q!: * $userID
        script:
        var user = $parseTree._userID;
        var url = "https://smartapp-code.sberdevices.ru/tools/api/data/$userId";
        var response = $http.get(url);
        if (response.isOk) {
            $channel = $request.channelUserId;
        }
        a: Привет!!! {{$channel}}        
        
    state: Дата
        intent!: /дата
        a: вы назвали дату {{ toPrettyString($parseTree._date) }}
        script: 
            $session.date = $parseTree._date.value;
            $session.year = $parseTree._date.year;
            $jsapi.log("--------дата: " + toPrettyString($parseTree) + " --------");
            $jsapi.log("--------дата(_date.value): " + toPrettyString($parseTree._date.value) + " --------");
            $jsapi.log("--------дата(_date.year): " + toPrettyString($parseTree._date.year) + " --------");

    state: Печать
        q!: печать
        a: дата: 
        a: {{ $session.date }}
        
    state: Год
        q!: год
        a: год:
        a: {{ $session.year }}

    state: Прощание
        intent!: /пока
        a: Пока пока

    state: Fallback
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}

