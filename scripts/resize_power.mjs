import sharp from 'sharp';
import path from 'path';

const input = process.argv[2];
const outputSmall = process.argv[3];
const outputLarge = process.argv[4];

async function resize() {
  await sharp(input)
    .resize(32, 32)
    .toFile(outputSmall);
  
  await sharp(input)
    .resize(84, 84)
    .toFile(outputLarge);
    
  console.log('Resized images successfully.');
}

resize().catch(err => {
  console.error(err);
  process.exit(1);
});
