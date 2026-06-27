# Distant Gunfire Sounds / Звуки дальней канонады

This folder contains distant gunfire sound effects that create a "cannonade" effect - sounds of weapons firing that can be heard from far away (up to 800 blocks).

Эта папка содержит звуки дальней стрельбы, которые создают эффект "канонады" - звуки оружия, которые слышны издалека (до 800 блоков).

## Required Sound Files / Необходимые звуковые файлы

You need to add the following .ogg sound files to this directory:

Вам нужно добавить следующие .ogg звуковые файлы в эту директорию:

1. **cannon.ogg** - Distant cannon fire sound (medium caliber, 15-39 damage weapons)
   - Звук дальнего выстрела из пушки (средний калибр, оружие с уроном 15-39)
   - Recommended: Deep, echoing boom sound
   - Рекомендуется: Глубокий, эхом отдающийся звук взрыва

2. **gunfire.ogg** - Distant gunfire sound (light weapons, <15 damage)
   - Звук дальней стрельбы (легкое оружие, урон <15)
   - Recommended: Sharp crack or pop sound
   - Рекомендуется: Резкий треск или хлопок

3. **tank_fire.ogg** - Distant heavy tank fire (heavy caliber, 40+ damage weapons)
   - Звук дальнего выстрела тяжелого танка (тяжелый калибр, оружие с уроном 40+)
   - Recommended: Very deep, powerful boom with long echo
   - Рекомендуется: Очень глубокий, мощный взрыв с длинным эхом

## Sound Characteristics / Характеристики звуков

- **Format**: OGG Vorbis
- **Sample Rate**: 44100 Hz recommended / Рекомендуется 44100 Гц
- **Channels**: Mono (1 channel) / Моно (1 канал)
- **Duration**: 1-3 seconds / Длительность: 1-3 секунды
- **Volume**: Should be normalized but not too loud / Должен быть нормализован, но не слишком громкий

## How It Works / Как это работает

When a weapon fires:
1. The normal close-range shoot sound plays immediately
2. The distant sound is sent to all players within 800 blocks
3. The sound is delayed based on distance (speed of sound simulation)
4. Volume decreases with distance (only audible from 80-800 blocks away)
5. Pitch is slightly lowered to simulate distance

Когда оружие стреляет:
1. Обычный звук выстрела вблизи воспроизводится сразу
2. Дальний звук отправляется всем игрокам в радиусе 800 блоков
3. Звук задерживается в зависимости от расстояния (симуляция скорости звука)
4. Громкость уменьшается с расстоянием (слышно только с 80-800 блоков)
5. Тон немного понижается для имитации расстояния

## Weapon Classification / Классификация оружия

- **Light weapons** (distant_gunfire): Machine guns, light cannons (damage < 15)
  - Легкое оружие: Пулеметы, легкие пушки (урон < 15)

- **Medium cannons** (distant_cannon): Aircraft cannons, medium tank guns (damage 15-39)
  - Средние пушки: Авиационные пушки, средние танковые орудия (урон 15-39)

- **Heavy cannons** (distant_tank_fire): Heavy tank guns, naval guns (damage 40+)
  - Тяжелые пушки: Тяжелые танковые орудия, корабельные орудия (урон 40+)

## Finding Sounds / Где найти звуки

You can find suitable sounds from:
- freesound.org (search for "distant gunfire", "cannon echo", "artillery distant")
- YouTube Audio Library
- Create your own by recording close sounds and adding reverb/echo effects

Вы можете найти подходящие звуки на:
- freesound.org (ищите "distant gunfire", "cannon echo", "artillery distant")
- Библиотека аудио YouTube
- Создайте свои, записав близкие звуки и добавив эффекты реверберации/эха

## Testing / Тестирование

To test the sounds in-game:
1. Place the .ogg files in this directory
2. Rebuild the mod
3. Fire weapons from different distances (80-800 blocks)
4. The distant sound should be audible with proper delay and attenuation

Для тестирования звуков в игре:
1. Поместите .ogg файлы в эту директорию
2. Пересоберите мод
3. Стреляйте из оружия с разных расстояний (80-800 блоков)
4. Дальний звук должен быть слышен с правильной задержкой и затуханием
