export const validatePassword = (password) => {
  const minLength = 8;
  const maxLength = 32;

  if (password.length < minLength || password.length > maxLength) {
    return `비밀번호는 ${minLength}자 이상 ${maxLength}자 이하이어야 합니다.`;
  }

  const hasUpperCase = /[A-Z]/.test(password);
  const hasLowerCase = /[a-z]/.test(password);
  const hasNumber = /[0-9]/.test(password);
  const hasSpecialChar = /[ !@#$%^&*()_+\-=\[\]{}|;':",./<>?]/.test(password);

  const typesCount = [hasUpperCase, hasLowerCase, hasNumber, hasSpecialChar].filter(Boolean).length;

  if (typesCount < 3) {
    return '비밀번호는 영문 대/소문자, 숫자, 특수문자 중 3가지 이상을 조합해야 합니다.';
  }

  return null; // 유효성 검사 통과
};

export const validateName = (name) => {
  if (!name) {
    return '이름을 입력해주세요.';
  }

  // 1. 허용 문자 검사 (한글, 영문, 숫자)
  if (/[^a-zA-Z0-9가-힣]/.test(name)) {
    return '이름은 한글, 영문, 숫자만 사용할 수 있습니다.';
  }

  // 2. 숫자만 있는지 검사
  if (/^[0-9]+$/.test(name)) {
    return '이름을 숫자로만 구성할 수 없습니다.';
  }

  const hasKorean = /[가-힣]/.test(name);
  const hasEnglish = /[a-zA-Z]/.test(name);

  // 3. 조합 및 길이 검사
  if (hasKorean && hasEnglish) { // 한글 + 영문 조합
    if (name.length < 3 || name.length > 8) {
      return '이름(한글+영문 조합)은 3자 이상 8자 이하이어야 합니다.';
    }
  } else if (hasKorean) { // 한글 또는 한글+숫자 조합
    if (name.length < 2 || name.length > 8) {
      return '한글 이름은 2자 이상 8자 이하이어야 합니다.';
    }
  } else if (hasEnglish) { // 영문 또는 영문+숫자 조합
    if (name.length < 3 || name.length > 13) {
      return '영문 이름은 3자 이상 13자 이하이어야 합니다.';
    }
  } else {
    // 이 경우는 숫자만 있는 경우인데, 위에서 이미 필터링됨
    return '유효하지 않은 이름 형식입니다.';
  }

  return null;
};
