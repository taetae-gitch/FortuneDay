import React, { useState } from "react";

export default function LoginForm() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        setMessage('');

        try {
            const response = await fetch('/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password })
            });

            const data = await response.json();

            if (response.ok) {
                // 로그인 성공
                localStorage.setItem('token', data.token);
                setMessage('로그인 성공!');
                console.log('로그인 성공:', data);
            } else {
                // 로그인 실패
                setMessage(data.message || '로그인에 실패했습니다.');
                console.error('로그인 실패:', data);
            }
        } catch (error) {
            setMessage('서버 연결 오류가 발생했습니다.');
            console.error('로그인 오류:', error);
        }
    }


    return(
        <div>
            <h2>로그인</h2>
            {message && <p style={{color: message.includes('성공') ? 'green' : 'red'}}>{message}</p>}
            <form onSubmit={handleLogin}>
                <div>
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required placeholder="이메일을 입력하세요."/>
                </div>
                <div>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required placeholder="비밀번호를 입력하세요." />
                </div>
                <button type="submit">로그인 하기</button>
            </form>
        </div>
    )

}