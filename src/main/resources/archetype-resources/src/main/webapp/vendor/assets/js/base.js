/**
 * Created by amssy on 2015/6/3.
 */
$(function($) {
    $(".nav-list li").click(function(){
        //alert($(this).children("ul"));
        if($(this).children("ul").css("display")=="block"){
            $(this).children("ul").css("display","none");
        }else{
            $(this).children("ul").css("display","block");
        }
        //$(".nav-list li").addClass('arrow');
    })
});