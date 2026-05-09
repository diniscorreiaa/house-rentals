No nosso trabalho nunca enviamos a resposta HTTP 201, no entanto poderia ser 
utilizadonas situações em que usamos enviamos código de resposta 200 para os métodos
POST e PUT.
Utilizámos 200 para as duas situações por questão de simplicidade, mas se quisessemos
dar uma resposta mais específica para POST's e PUT's poderíamos ter usado o código 201
para indicar que o recurso foi criado/atualizado.
