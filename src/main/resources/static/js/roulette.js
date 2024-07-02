const $c = document.querySelector("canvas");
const ctx = $c.getContext(`2d`);
const menuAdd = document.querySelector('#menuAdd');
const product = [];
const colors = [];

const newMake = () => {
  const [cw, ch] = [$c.width / 2, $c.height / 2];
  const arc = Math.PI / (product.length / 2);  
  for (let i = 0; i < product.length; i++) {
    ctx.beginPath();
    if(colors.length == 0){
      for(var l=0; l<product.length; l++){
        let r = Math.floor(Math.random() * 256);
        let g = Math.floor(Math.random() * 256);
        let b = Math.floor(Math.random() * 256);
        colors.push("rgb(" + r + "," + g + "," + b + ")");
      }
    }
    ctx.fillStyle = colors[i % (colors.length)];
    ctx.moveTo(cw, ch);
    ctx.arc(cw, ch, cw, arc * (i - 1), arc * i);
    ctx.fill();
    ctx.closePath();
  }

  ctx.fillStyle = "#fff";
  ctx.font = "18px Pretendard";
  ctx.textAlign = "center";

  for (let i = 0; i < product.length; i++) {
    const angle = (arc * i) + (arc / 2);

    ctx.save();

    ctx.translate(
      cw + Math.cos(angle) * (cw - 50),
      ch + Math.sin(angle) * (ch - 50)
    );

    ctx.rotate(angle + Math.PI / 2);

    product[i].split(" ").forEach((text, j) => {
      ctx.fillText(text, 0, 30 * j);
    });

    ctx.restore();
  }
}

const rotate = () => {
  $c.style.transform = `initial`;
  $c.style.transition = `initial`;
  const alpha = Math.floor(Math.random()*100);

  setTimeout(() => {    
    const ran = Math.floor(Math.random() * product.length);
    const arc = 360 / product.length;
    const rotate = (ran * arc) + 3600 + (arc * 3) - (arc/4) + alpha;
    $c.style.transform = `rotate(-${rotate}deg)`;
    $c.style.transition = `2s`;
    
  }, 1);
};


function add(){
  if(menuAdd.value != undefined && menuAdd.value != ""){
    product.push(menuAdd.value);
    let r = Math.floor(Math.random() * 256);
    let g = Math.floor(Math.random() * 256);
    let b = Math.floor(Math.random() * 256);
    colors.push("rgb(" + r + "," + g + "," + b + ")");
    newMake();
    menuAdd.value="";
  }
  else{
    alert("메뉴를 입력한 후 버튼을 클릭 해 주세요");
  }
}

newMake();