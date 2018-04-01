/**
 * Created by shanks on 2017/3/13.
 */
function DisableElements(container,blnHidenButton)
{
    if (!container)
        return;
    var aEle;
    if (navigator.appName =="Microsoft Internet Explorer") //IE
    {
        for (var i=0;i<container.all.length;i++)
        {
            aEle = container.all[i];
            tagName = aEle.tagName.toUpperCase();
            if ((tagName=="SELECT"))
            {
                aEle.disabled = true;
                if(tagName=="BUTTON" && blnHidenButton)
                {
                    //aEle.style.display = "none";//对button不做处理
                }
            }
            else if (tagName=="INPUT")
            {
                if (aEle.type.toUpperCase()!="HIDDEN")
                {
                    if (aEle.type.toUpperCase()=="TEXT")
                    {
                        ReadonlyText(aEle);
                    }
                    else if (aEle.type.toUpperCase()=="BUTTON")
                    {
                        //do nothing;
                    }
                    else
                    {
                        aEle.disabled = true;
                    }
                }
                if((aEle.type.toUpperCase()=="BUTTON"||aEle.type.toUpperCase()=="SUBMIT") && blnHidenButton)
                {
                    //aEle.style.display = "none";//对button不处理
                }
            }
            else if (tagName=="TEXTAREA")
            {
                ReadonlyText(aEle);
            }
        }
    }
    else//非IE浏览器
    {
        var aEle = container.getElementsByTagName("select");
        for (var i=0;i< aEle.length;i++)
        {
            aEle[i].disabled = true;
        }
        aEle = container.getElementsByTagName("button");
        for (var i=0;i< aEle.length;i++)
        {
            aEle[i].disabled = true;
        }
        aEle = container.getElementsByTagName("textarea");
        for (var i=0;i< aEle.length;i++)
        {
            ReadonlyText(aEle[i]);
        }
        aEle = container.getElementsByTagName("input");
        for (var i=0;i< aEle.length;i++)
        {
            if (aEle[i].type.toUpperCase()!="HIDDEN")
            {
                if (aEle[i].type.toUpperCase()=="TEXT")
                {
                    ReadonlyText(aEle[i]);
                }
                else
                {
                    aEle[i].disabled = true;
                }
            }
            if((aEle[i].type.toUpperCase()=="BUTTON"||aEle[i].type.toUpperCase()=="SUBMIT")&&blnHidenButton)
            {
                aEle[i].style.display = "none";
            }
        }
    }
}
function ReadonlyText(objText)
{
    if (objText){
        //objText.style.backgroundColor = "menu";
        objText.style.background = "#E6E6E6";
        //objText.style.color = "black";
        objText.readOnly=true;
    }
}
