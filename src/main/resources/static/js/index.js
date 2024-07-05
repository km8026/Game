document.addEventListener("DOMContentLoaded", function () {
  const progressBar = document.querySelector('.progress-bar');
  const loadingScreen = document.querySelector('#loading-screen');
  const delay = () => {
    return new Promise(resolve => setTimeout(resolve, 600));
  }

  const hideProgressBar = async () => {
    await delay();
    progressBar.style.display = 'none';
  }
  
  const init = async () => {
    await delay();
    await hideProgressBar();
    const curtainLeft = document.querySelector('.curtain-left');
    const curtainRight = document.querySelector('.curtain-right');
    const content = document.getElementById('content');

    curtainLeft.style.transform = 'translateX(-100%)';
    curtainRight.style.transform = 'translateX(100%)';

    content.classList.add('show');

    setTimeout(() => {
      loadingScreen.style.display = 'none';
    }, 600);
  };
  init();
});