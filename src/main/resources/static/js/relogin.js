$(document).ready(function() {
    // contact-link 클릭 이벤트 처리
    $('.contact-link').click(function(event) {
      // AJAX 요청을 통해 로그인 상태 확인
      $.ajax({
        type: 'GET',
        url: '/users/checkLoginStatus', // 사용자 로그인 상태 확인 컨트롤러 경로
        success: function(response) {
          if (response.loggedIn) {
            // 로그인 상태인 경우, 외부 링크로 이동
            window.location.href = 'https://cheongwon.go.kr/portal';
          } else {
            // 로그인되어 있지 않은 경우, 로그인 페이지로 이동
            window.location.href = '/users/login';
          }
        },
        error: function(xhr, status, error) {
          console.error('Error checking login status:', error);
        }
      });
  
      // 기본 클릭 동작을 막습니다.
      event.preventDefault();
    });
  
    // gameboy6_link 클릭 이벤트 처리
    $('.gameboy6_link').click(function(event) {
      // AJAX 요청을 통해 로그인 상태 확인
      $.ajax({
        type: 'GET',
        url: '/users/checkLoginStatus', // 사용자 로그인 상태 확인 컨트롤러 경로
        success: function(response) {
          if (response.loggedIn) {
            // 로그인 상태인 경우, 외부 링크로 이동
            window.location.href = 'https://pokerogue.net/';
          } else {
            // 로그인되어 있지 않은 경우, 로그인 페이지로 이동
            window.location.href = '/users/login';
          }
        },
        error: function(xhr, status, error) {
          console.error('Error checking login status:', error);
        }
      });
  
      // 기본 클릭 동작을 막습니다.
      event.preventDefault();
    });
  });
  