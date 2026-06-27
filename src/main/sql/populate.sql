-- THIS DB POPULATE SCRIPT WAS ENTIRELY GENERATED WITH ARTIFICIAL INTELLIGENCE.

-- Clean up existing data and reset identity sequences
TRUNCATE TABLE BOOKING, HOUSE, LOCATION, "user" RESTART IDENTITY CASCADE;

-- ==========================================
-- 1. USERS
-- ==========================================
INSERT INTO "user" ("UserName", "Email", "Password") VALUES
('João Silva', 'joao.silva@example.pt', 'hash_pass_1'),
('Maria Santos', 'maria.santos@example.pt', 'hash_pass_2'),
('António Costa', 'antonio.costa@example.pt', 'hash_pass_3'),
('Ana Ferreira', 'ana.ferreira@example.pt', 'hash_pass_4'),
('Pedro Gomes', 'pedro.gomes@example.pt', 'hash_pass_5'),
('Catarina Almeida', 'catarina.almeida@example.pt', 'hash_pass_6'),
('Rui Rodrigues', 'rui.rodrigues@example.pt', 'hash_pass_7'),
('Sofia Martins', 'sofia.martins@example.pt', 'hash_pass_8'),
('Miguel Pereira', 'miguel.pereira@example.pt', 'hash_pass_9'),
('Beatriz Ribeiro', 'beatriz.ribeiro@example.pt', 'hash_pass_10');

-- ==========================================
-- 2. LOCATIONS
-- ==========================================
-- Country (ID: 1)
INSERT INTO LOCATION ("LocationName", "Type", "ParentLocation")
VALUES ('Portugal', 'COUNTRY', NULL);

-- Districts (IDs: 2 to 19)
INSERT INTO LOCATION ("LocationName", "Type", "ParentLocation") VALUES
('Aveiro', 'DISTRICT', 1), ('Beja', 'DISTRICT', 1), ('Braga', 'DISTRICT', 1),
('Bragança', 'DISTRICT', 1), ('Castelo Branco', 'DISTRICT', 1), ('Coimbra', 'DISTRICT', 1),
('Évora', 'DISTRICT', 1), ('Faro', 'DISTRICT', 1), ('Guarda', 'DISTRICT', 1),
('Leiria', 'DISTRICT', 1), ('Lisboa', 'DISTRICT', 1), ('Portalegre', 'DISTRICT', 1),
('Porto', 'DISTRICT', 1), ('Santarém', 'DISTRICT', 1), ('Setúbal', 'DISTRICT', 1),
('Viana do Castelo', 'DISTRICT', 1), ('Vila Real', 'DISTRICT', 1), ('Viseu', 'DISTRICT', 1);

-- Municipalities of Lisboa District - ParentLocation 12 (IDs: 20 to 35)
INSERT INTO LOCATION ("LocationName", "Type", "ParentLocation") VALUES
('Alenquer', 'MUNICIPALITY', 12), ('Amadora', 'MUNICIPALITY', 12),
('Arruda dos Vinhos', 'MUNICIPALITY', 12), ('Azambuja', 'MUNICIPALITY', 12),
('Cadaval', 'MUNICIPALITY', 12), ('Cascais', 'MUNICIPALITY', 12),
('Lisboa', 'MUNICIPALITY', 12), ('Loures', 'MUNICIPALITY', 12),
('Lourinhã', 'MUNICIPALITY', 12), ('Mafra', 'MUNICIPALITY', 12),
('Odivelas', 'MUNICIPALITY', 12), ('Oeiras', 'MUNICIPALITY', 12),
('Sintra', 'MUNICIPALITY', 12), ('Sobral de Monte Agraço', 'MUNICIPALITY', 12),
('Torres Vedras', 'MUNICIPALITY', 12), ('Vila Franca de Xira', 'MUNICIPALITY', 12);

-- Localities
-- Lisboa Municipality - ParentLocation 26 (IDs: 36 to 59)
INSERT INTO LOCATION ("LocationName", "Type", "ParentLocation") VALUES
('Ajuda', 'LOCALITY', 26), ('Alcântara', 'LOCALITY', 26), ('Alvalade', 'LOCALITY', 26),
('Areeiro', 'LOCALITY', 26), ('Arroios', 'LOCALITY', 26), ('Avenidas Novas', 'LOCALITY', 26),
('Beato', 'LOCALITY', 26), ('Belém', 'LOCALITY', 26), ('Benfica', 'LOCALITY', 26),
('Campo de Ourique', 'LOCALITY', 26), ('Campolide', 'LOCALITY', 26), ('Carnide', 'LOCALITY', 26),
('Estrela', 'LOCALITY', 26), ('Lumiar', 'LOCALITY', 26), ('Marvila', 'LOCALITY', 26),
('Misericórdia', 'LOCALITY', 26), ('Olivais', 'LOCALITY', 26), ('Parque das Nações', 'LOCALITY', 26),
('Penha de França', 'LOCALITY', 26), ('Santa Clara', 'LOCALITY', 26), ('Santa Maria Maior', 'LOCALITY', 26),
('Santo António', 'LOCALITY', 26), ('São Domingos de Benfica', 'LOCALITY', 26), ('São Vicente', 'LOCALITY', 26);

-- Cascais Municipality - ParentLocation 25 (IDs: 60 to 63)
INSERT INTO LOCATION ("LocationName", "Type", "ParentLocation") VALUES
('Alcabideche', 'LOCALITY', 25), ('Carcavelos e Parede', 'LOCALITY', 25),
('Cascais e Estoril', 'LOCALITY', 25), ('São Domingos de Rana', 'LOCALITY', 25);

-- Oeiras Municipality - ParentLocation 31 (IDs: 64 to 68) -- FIX: Shortened to <30 chars
INSERT INTO LOCATION ("LocationName", "Type", "ParentLocation") VALUES
('Algés, L. Velha e C. Quebrada', 'LOCALITY', 31), ('Barcarena', 'LOCALITY', 31),
('Carnaxide e Queijas', 'LOCALITY', 31), ('Oeiras, S. Julião e P. Arcos', 'LOCALITY', 31),
('Porto Salvo', 'LOCALITY', 31);

-- Amadora Municipality - ParentLocation 21 (IDs: 69 to 74)
INSERT INTO LOCATION ("LocationName", "Type", "ParentLocation") VALUES
('Águas Livres', 'LOCALITY', 21), ('Alfragide', 'LOCALITY', 21),
('Encosta do Sol', 'LOCALITY', 21), ('Falagueira-Venda Nova', 'LOCALITY', 21),
('Mina de Água', 'LOCALITY', 21), ('Venteira', 'LOCALITY', 21);

-- Sintra Municipality - ParentLocation 32 (IDs: 75 to 85) -- FIX: Shortened to <30 chars
INSERT INTO LOCATION ("LocationName", "Type", "ParentLocation") VALUES
('Agualva e Mira-Sintra', 'LOCALITY', 32), ('Algueirão-Mem Martins', 'LOCALITY', 32),
('Almargem do Bispo, P. Pinheiro', 'LOCALITY', 32), ('Cacém e São Marcos', 'LOCALITY', 32),
('Casal de Cambra', 'LOCALITY', 32), ('Colares', 'LOCALITY', 32),
('Massamá e Monte Abraão', 'LOCALITY', 32), ('Queluz e Belas', 'LOCALITY', 32),
('Rio de Mouro', 'LOCALITY', 32), ('S. João das Lampas e Terrugem', 'LOCALITY', 32),
('Sintra', 'LOCALITY', 32);

-- Loures Municipality - ParentLocation 27 (IDs: 86 to 95)
INSERT INTO LOCATION ("LocationName", "Type", "ParentLocation") VALUES
('Bucelas', 'LOCALITY', 27), ('Camarate, Unhos e Apelação', 'LOCALITY', 27),
('Fanhões', 'LOCALITY', 27), ('Loures', 'LOCALITY', 27),
('Lousa', 'LOCALITY', 27), ('Moscavide e Portela', 'LOCALITY', 27),
('Sacavém e Prior Velho', 'LOCALITY', 27), ('Santa Iria de Azoia', 'LOCALITY', 27),
('São João da Talha e Bobadela', 'LOCALITY', 27), ('Santo António dos Cavaleiros', 'LOCALITY', 27);

-- Odivelas Municipality - ParentLocation 30 (IDs: 96 to 99) -- FIX: Shortened to <30 chars
INSERT INTO LOCATION ("LocationName", "Type", "ParentLocation") VALUES
('Odivelas', 'LOCALITY', 30), ('Pontinha e Famões', 'LOCALITY', 30),
('P. Sto Adrião e Olival Basto', 'LOCALITY', 30), ('Ramada e Caneças', 'LOCALITY', 30);

-- Remaining Municipalities Localities (IDs: 100 to 108) -- FIX: Shortened to <30 chars
INSERT INTO LOCATION ("LocationName", "Type", "ParentLocation") VALUES
('Alenquer (Freguesia)', 'LOCALITY', 20),
('Arruda dos Vinhos (Freguesia)', 'LOCALITY', 22),
('Azambuja (Freguesia)', 'LOCALITY', 23),
('Cadaval (Freguesia)', 'LOCALITY', 24),
('Lourinhã (Freguesia)', 'LOCALITY', 28),
('Mafra (Freguesia)', 'LOCALITY', 29),
('Sobral de M. Agraço (Freg.)', 'LOCALITY', 33),
('Torres Vedras (Freguesia)', 'LOCALITY', 34),
('V. Franca de Xira (Freg.)', 'LOCALITY', 35);

-- ==========================================
-- 3. HOUSES (100 Records)
-- ==========================================
INSERT INTO HOUSE ("Title", "Location", "Area", "Price", "Description", "Owner") VALUES
('T2 com vista para o rio', 43, 85.5, 140.0, 'Excelente apartamento em Belém. Cozinha equipada, casa de banho renovada e ecrã plano na sala. Ótimo estado de conservação.', 1),
('Moradia Isolada T4', 62, 250.0, 350.0, 'Fantástica moradia em Cascais com piscina, jardim e garagem dupla. 3 casas de banho completas.', 2),
('Apartamento T1 Renovado', 38, 55.0, 95.0, 'T1 muito acolhedor em Alvalade. Conta com um quarto luminoso, casa de banho moderna e Wi-Fi excelente para o seu telemóvel ou portátil.', 3),
('Estúdio prático', 57, 35.0, 60.0, 'Estúdio funcional no centro. Ideal para escapadinhas de fim de semana. Tem tudo o que precisa num espaço bem aproveitado.', 4),
('T3 de Luxo na Lapa', 48, 140.0, 220.0, 'Fabuloso apartamento na Estrela. Ótimos acabamentos, suíte master e varanda com vista desafogada.', 5),
('Quintinha rústica', 80, 500.0, 180.0, 'Refúgio em Colares. Moradia rústica inserida numa zona de muito sossego e natureza exuberante.', 6),
('T2 moderno c/ garagem', 53, 90.0, 110.0, 'Apartamento no Parque das Nações. Prédio com segurança, lugar de garagem e acesso ao ginásio do condomínio.', 7),
('T0 no Bairro Alto', 47, 40.0, 75.0, 'Apartamento compacto e vibrante na Misericórdia. Vida noturna à porta e excelente localização histórica.', 8),
('Penthouse T4 Duplex', 66, 320.0, 450.0, 'Exclusivo imóvel em Oeiras. Terraço panorâmico, 4 casas de banho, domótica avançada em todo o apartamento.', 9),
('Moradia em banda T3', 89, 130.0, 125.0, 'Em Loures, zona calma familiar. Possui logradouro espaçoso e sala com lareira para os dias frios.', 10),
('T1 acolhedor na Baixa', 56, 45.0, 85.0, 'Em Santa Maria Maior. Renovação respeitando a arquitetura tradicional pombalina. Chão de tábua corrida.', 1),
('T3 espaçoso c/ varanda', 44, 110.0, 135.0, 'Benfica. Perto de transportes, prédio tranquilo, cozinha de origem mas em ótimo estado de conservação.', 2),
('Apartamento T2 Perto Metro', 54, 78.0, 105.0, 'A apenas dois passos da estação da Penha de França. Cozinha semi-equipada.', 3),
('Loft industrial', 50, 95.0, 150.0, 'Marvila. Antigo armazém convertido num fabuloso espaço aberto. Pé-direito alto e muita luz.', 4),
('T2 com terraço', 96, 82.0, 95.0, 'Odivelas centro. Conta com um magnífico terraço de 30m2 para aproveitar o sol da tarde.', 5),
('Moradia T5 com Terreno', 105, 450.0, 300.0, 'Mafra. Moradia rústica com excelentes áreas, forno a lenha, e terreno com árvores de fruto.', 6),
('T1 luminoso', 73, 60.0, 70.0, 'Mina de Água, Amadora. Ótima exposição solar, prédio com elevador e zona de comércio próxima.', 7),
('Apartamento T3 Novo', 64, 120.0, 180.0, 'Algés. Construção recente, isolamento térmico fabuloso, painéis solares e acabamentos de luxo.', 8),
('T0 para estudantes', 46, 30.0, 50.0, 'Campolide, próximo à universidade. Simples, prático e em ótimo estado.', 9),
('T2 Premium no Estoril', 62, 100.0, 200.0, 'Vista frontal de mar. Condomínio fechado com piscina aquecida e segurança 24h.', 10),
('T4 Familiar', 49, 145.0, 160.0, 'Lumiar, próximo a parques verdes. Excelentes áreas e três casas de banho.', 1),
('T1 Vista Castelo', 58, 50.0, 110.0, 'São Vicente, Alfama. A vista mais romântica de Lisboa a partir da varanda do seu quarto.', 2),
('Moradia geminada T3', 63, 115.0, 130.0, 'São Domingos de Rana. Boa oportunidade. Zona de churrasqueira no pátio traseiro.', 3),
('T2 Charmoso', 45, 75.0, 100.0, 'Campo de Ourique. Bairro vibrante, perto do mercado. Prédio recuperado.', 4),
('T3 com suíte', 79, 112.0, 115.0, 'Casal de Cambra. Apartamento familiar em zona de rápido desenvolvimento.', 5),
('T0 no Marquês', 57, 38.0, 85.0, 'Santo António. O verdadeiro coração da cidade. Decoração cuidada e moderna.', 6),
('Apartamento T1 com pátio', 52, 65.0, 90.0, 'Olivais. Rés-do-chão acolhedor com um agradável pátio privativo nas traseiras.', 7),
('T4 na Linha', 61, 160.0, 210.0, 'Carcavelos. 5 minutos a pé da praia. Ideal para quem procura qualidade de vida.', 8),
('Moradia T2 Histórica', 106, 90.0, 110.0, 'Sobral de Monte Agraço. Recuperada mantendo a traça original e pedra exposta.', 9),
('T3 Varandas do Rio', 37, 130.0, 175.0, 'Alcântara. Vista de primeira linha para a Ponte 25 de Abril e o Tejo.', 10),
('Apartamento T2', 65, 88.0, 115.0, 'Barcarena. Inserido num ambiente tranquilo, mas perto de acessos a Lisboa.', 1),
('T1 remodelado a estrear', 42, 58.0, 98.0, 'Beato. Uma zona em forte expansão. A cozinha e a casa de banho são totalmente novas.', 2),
('T3 Vista Serra', 85, 125.0, 140.0, 'Sintra. Apartamento muito cuidado com vista fabulosa para o Palácio da Pena.', 3),
('T2 Areeiro', 39, 80.0, 120.0, 'Prédio clássico com dois elevadores. Chão original mantido e envernizado.', 4),
('Moradia Isolada T5', 104, 380.0, 310.0, 'Lourinhã. A poucos minutos do mar. Extenso relvado e piscina de água salgada.', 5),
('T1 Design', 41, 55.0, 105.0, 'Avenidas Novas. Estilo minimalista com uma televisão de ecrã plano de 65 polegadas.', 6),
('T0 Moderno', 71, 35.0, 65.0, 'Encosta do Sol. Pequeno mas com ótima organização do espaço e muita luz.', 7),
('T4 com Piscina Privativa', 60, 220.0, 380.0, 'Alcabideche. Arquitetura moderna, janelas amplas e domótica.', 8),
('Apartamento T2 Clássico', 40, 85.0, 110.0, 'Arroios. Tetos trabalhados e portas de época num dos bairros mais dinâmicos.', 9),
('T3 c/ Estacionamento', 93, 105.0, 105.0, 'Santa Iria de Azoia. Apartamento prático e em ótimo estado de conservação.', 10),
('T1 com Mezanine', 56, 52.0, 95.0, 'Santa Maria Maior. Aproveitamento excelente de pé direito alto.', 1),
('T2 Varanda Envidraçada', 81, 78.0, 85.0, 'Massamá. Próximo à estação de comboios, sala espaçosa com marquise aproveitada.', 2),
('T3 em Condomínio', 67, 135.0, 190.0, 'Paço de Arcos. Segurança 24h, cortes de ténis e duas vagas de garagem.', 3),
('Moradia Rústica T2', 103, 95.0, 80.0, 'Cadaval. Ideal para fugir do stress da cidade e aproveitar a natureza.', 4),
('Apartamento T4+', 53, 200.0, 250.0, 'Parque das Nações. Duas suítes, sala de 50m2, e varanda com vista rio fabulosa.', 5),
('T1 Novo', 90, 60.0, 85.0, 'Lousa, Loures. Construção de 2025. Cozinha totalmente equipada (Bosch).', 6),
('T2 Charmoso em Benfica', 58, 80.0, 105.0, 'São Domingos de Benfica. Remodelação de muito bom gosto.', 7),
('Estúdio Premium', 26, 45.0, 130.0, 'No centro de Lisboa. Serviço de limpeza diário e portaria incluídos.', 8),
('Moradia T4 Contemporânea', 76, 210.0, 260.0, 'Algueirão-Mem Martins. Acabamentos de luxo, aspiração central e painéis solares.', 9),
('T3 com Jardim', 62, 140.0, 210.0, 'Cascais e Estoril. Apartamento térreo com acesso exclusivo a um jardim fabuloso.', 10),
('T1 Renovado', 38, 55.0, 95.0, 'Alvalade. Quarto luminoso, casa de banho moderna.', 1),
('T3 Vista Cidade', 47, 110.0, 140.0, 'Misericórdia. Apartamento no último andar com vista soberba.', 2),
('Moradia T3', 100, 150.0, 130.0, 'Alenquer. Inserida numa vila encantadora e com um terraço soalheiro.', 3),
('T2 Acolhedor', 84, 85.0, 95.0, 'São João das Lampas. Ótimo estado, lugar de garagem e arrumos.', 4),
('T0 Central', 57, 40.0, 80.0, 'Santo António. A 5 minutos a pé do metro do Marquês de Pombal.', 5),
('T4 Familiar e Espaçoso', 92, 150.0, 160.0, 'Sacavém. Prédio recente, com varandas generosas e muita luz solar.', 6),
('Apartamento T1 Moderno', 64, 60.0, 100.0, 'Algés. Muito procurado, excelente construção e localização fantástica.', 7),
('T3 na Penha', 54, 100.0, 125.0, 'Penha de França. Bairro tradicional, apartamento com duas frentes.', 8),
('Moradia T4', 107, 240.0, 220.0, 'Torres Vedras. Próximo aos acessos da A8. Sala ampla e jardim fabuloso.', 9),
('T2 com Lareira', 83, 90.0, 105.0, 'Rio de Mouro. Imóvel muito bem estimado com varanda na sala.', 10),
('T1 Perto do Rio', 37, 50.0, 90.0, 'Alcântara. Excelente imóvel de investimento ou primeira habitação.', 1),
('Estúdio Minimalista', 50, 35.0, 75.0, 'Marvila. Em zona trendy, num edifício reabilitado.', 2),
('T3 no Centro', 96, 115.0, 115.0, 'Odivelas. Fica mesmo no centro, com acesso a todos os serviços e comércio.', 3),
('Apartamento T4 Duplex', 48, 180.0, 280.0, 'Estrela. Charme e requinte. Três excelentes casas de banho e tetos altos.', 4),
('Moradia T3 Isolada', 101, 160.0, 145.0, 'Arruda dos Vinhos. Excelentes vistas sobre os vales e vinhas da região.', 5),
('T2 Remodelado', 43, 75.0, 125.0, 'Belém. Perto do Mosteiro dos Jerónimos, cozinha americana.', 6),
('T1 Elegante', 41, 60.0, 110.0, 'Avenidas Novas. Edifício de prestígio com porteiro e segurança.', 7),
('T0 Prático', 46, 38.0, 60.0, 'Campolide. Rés do chão elevado, virado a sul.', 8),
('T3 com Varanda', 72, 105.0, 100.0, 'Falagueira. Ótimo apartamento de família, com parque infantil na praceta.', 9),
('Moradia V4 Luxo', 68, 300.0, 450.0, 'Porto Salvo. Num dos melhores condomínios privados da linha.', 10),
('T2 Duplex', 58, 85.0, 130.0, 'São Vicente. Bairro da Graça, zona muito tranquila.', 1),
('Apartamento T1', 77, 55.0, 75.0, 'Almargem do Bispo. Bem conservado, com pequena varanda no quarto.', 2),
('T3 Espaçoso', 95, 120.0, 115.0, 'Santo António dos Cavaleiros. Dispõe de duas despensas e lugar de parqueamento.', 3),
('Moradia em Banda T3', 102, 140.0, 135.0, 'Azambuja. Pronta a habitar, logradouro com churrasqueira.', 4),
('T2 Vista Mar', 61, 95.0, 160.0, 'Carcavelos. Acordar todos os dias a ver o mar, apartamento fabuloso.', 5),
('T1 com Parqueamento', 39, 62.0, 105.0, 'Areeiro. Apartamento confortável no coração do bairro.', 6),
('T4 Fabuloso', 53, 190.0, 260.0, 'Parque das Nações. Acabamentos de topo, soalho em madeira maciça.', 7),
('Estúdio Luminoso', 40, 40.0, 80.0, 'Arroios. Orientação solar sul, muita luz natural todo o dia.', 8),
('T3 Remodelado', 88, 105.0, 110.0, 'Fanhões, Loures. Cozinha, canalização e sistema elétrico novos.', 9),
('Moradia T5', 108, 350.0, 280.0, 'Vila Franca de Xira. Terreno murado, piscina e grande privacidade.', 10),
('T2 no Centro', 55, 80.0, 95.0, 'Santa Clara. Junto a transportes públicos, facilidade de estacionamento.', 1),
('T1 Acolhedor', 51, 50.0, 75.0, 'Carnide. Numa das praças centrais, bairro com espírito de aldeia.', 2),
('Apartamento T3', 97, 110.0, 105.0, 'Pontinha. Inserido num 2.º andar de um prédio tranquilo sem elevador.', 3),
('T0 Renovado', 47, 30.0, 70.0, 'Bairro Alto, Misericórdia. Perfeito para rentabilização imediata.', 4),
('Moradia T4 com Jardim', 78, 200.0, 240.0, 'Cacém e São Marcos. Perto da via rápida e zonas comerciais, zona sossegada.', 5),
('T2 com Excelentes Áreas', 45, 90.0, 135.0, 'Campo de Ourique. Suíte e casa de banho social, sala fantástica.', 6),
('T1 Vista Rio', 37, 55.0, 115.0, 'Alcântara. Junto às Docas, janela panorâmica na sala de estar.', 7),
('T3 Novo', 99, 130.0, 145.0, 'Ramada e Caneças. Nova urbanização, vista livre para a cidade de Lisboa.', 8),
('Estúdio Moderno', 57, 35.0, 90.0, 'Santo António. Mobilado com muito bom gosto, ecrã plano fixo na parede.', 9),
('Moradia T3', 60, 145.0, 210.0, 'Alcabideche. Arquitetura em L, com piscina abrigada do vento.', 10),
('T4 de Charme', 48, 175.0, 245.0, 'Estrela. Portadas em madeira e chão de ripas largas. Lareira.', 1),
('T2 Próximo Estação', 82, 85.0, 95.0, 'Queluz. Apenas 2 minutos da CP. Muito prático para deslocações.', 2),
('T1 com Terraço', 56, 50.0, 120.0, 'Santa Maria Maior. O terraço é ideal para aproveitar as noites de verão.', 3),
('T3 Condomínio Fechado', 65, 140.0, 170.0, 'Barcarena. Com campo de jogos, piscina e segurança no condomínio.', 4),
('Apartamento T2', 86, 75.0, 85.0, 'Bucelas. Inserido na capital do Arinto, ambiente calmo e verdejante.', 5),
('T0 no R/C', 44, 38.0, 75.0, 'Benfica. Totalmente livre de humidades, casa de banho ampla.', 6),
('Moradia T4+1', 63, 250.0, 280.0, 'São Domingos de Rana. Cave aproveitada com mesa de bilhar e bar.', 7),
('T1 Funcional', 52, 58.0, 80.0, 'Olivais. Perto do shopping, janelas de vidro duplo.', 8),
('T2 com Marquise', 94, 80.0, 90.0, 'São João da Talha. Cozinha espaçosa, despensa grande.', 9),
('T3 Luxuoso', 62, 160.0, 320.0, 'Cascais e Estoril. No coração de Cascais, 3 suítes, vistas espetaculares.', 10);

-- ==========================================
-- 4. BOOKINGS (Dynamic generation)
-- ==========================================
DO $$
DECLARE
    h_id INT;
    u_id INT;
    num_bookings INT;
    b_start DATE;
    b_end DATE;
    timeline_cursor DATE;
    max_date DATE := '2027-06-27'::DATE;
    duration INT;
    gap INT;
BEGIN
    FOR h_id IN 1..100 LOOP
        num_bookings := floor(random() * 20 + 1)::INT;
        timeline_cursor := '2026-06-27'::DATE + floor(random() * 10)::INT;

        FOR i IN 1..num_bookings LOOP
            u_id := floor(random() * 10 + 1)::INT;
            duration := floor(random() * 14 + 1)::INT;

            b_start := timeline_cursor;
            b_end := b_start + duration;

            IF b_end > max_date THEN
                EXIT;
            END IF;

            INSERT INTO BOOKING ("StartDate", "EndDate", "IdUser", "IdHouse")
            VALUES (b_start, b_end, u_id, h_id);

            gap := floor(random() * 15 + 1)::INT;
            timeline_cursor := b_end + gap;
        END LOOP;
    END LOOP;
END $$;
