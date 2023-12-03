from PIL import Image, ImageDraw, ImageEnhance


tfc_fruits = ('cherry', 'green_apple', 'lemon', 'olive', 'orange', 'peach', 'plum', 'red_apple')


def main():
    for fruit in tfc_fruits:
        for life in ('', '_fruiting', '_flowering', '_dry'):
            fluff('%s%s_leaves' % (fruit, life))
            
def fluff(file):
    img = Image.open(file + '.png').convert('RGBA')
    bg = Image.new('RGBA', (32, 32), (0, 0, 0, 0))
    for loc in ((0, 0), (16, 0), (0, 16), (16, 16)):
        bg.paste(img, loc, img)
    rad = int(bg.width / 2)
    for x in range(0, bg.width):
        for y in range(0, bg.height):
            xr = x - 16
            yr = y - 16
            if xr * xr + yr * yr > rad * rad:
                bg.putpixel((x, y), (0, 0, 0, 0))
    
    bg.save(file + '_fluff.png')
    

if __name__ == '__main__':
    main()