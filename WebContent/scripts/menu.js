img_names = new Array(
	"images/common/triangle.gif",
	"images/common/bullet.png"
);

loadImages();

function loadImages()
{
  img = new Array(img_names.length);
  for(i = 0; i < img.length; i++)
  {
	img[i] = new Image();
  	img[i].src = img_names[i];
  }
}

  function overMenuItem(ob)
  {
      //ob.style.backgroundColor="rgb(227,227,240)"; //"rgb(204,204,204)";
      /*ob.style.backgroundColor="white";
      ob.style.fontWeight="bold";
      ob.style.borderWidth="0";*/

  }
  function outMenuItem(ob)
  {
     
      /*ob.style.backgroundColor="rgb(217, 249, 220)";

      ob.style.fontWeight="normal";
      ob.style.borderWidth="0";*/

  }

  function showPullDown(s)
  {
	   if (eval(s).style.display =='none')
	      eval(s).style.display ='';
	   else
	      eval(s).style.display ='none';
  }
