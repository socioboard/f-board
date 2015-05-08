/*
 * This file is part of the SDWebImage package.
 * (c) Olivier Poitrey <rs@dailymotion.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

#import "UIImageView+WebCache.h"

@implementation UIImageView (WebCache)

- (void)setImageWithURL:(NSURL *)url
{
    [self setImageWithURL:url placeholderImage:nil];
}

- (void)setImageWithURL:(NSURL *)url placeholderImage:(UIImage *)placeholder
{
    [self setImageWithURL:url placeholderImage:placeholder options:0];
}

- (void)setImageWithURL:(NSURL *)url placeholderImage:(UIImage *)placeholder options:(SDWebImageOptions)options
{
    SDWebImageManager *manager = [SDWebImageManager sharedManager];

    // Remove in progress downloader from queue
    [manager cancelForDelegate:self];

    self.image = placeholder;

    if (url)
    {
        [manager downloadWithURL:url delegate:self options:options];
    }
}

- (void)cancelCurrentImageLoad
{
	UIProgressView *prg = (UIProgressView *)[self viewWithTag:kSDWebImageProgressView];
	prg.hidden = YES;
    [[SDWebImageManager sharedManager] cancelForDelegate:self];
}

- (void)webImageManager:(SDWebImageManager *)imageManager didFinishWithImage:(UIImage *)image
{
    self.image = image;
	UIProgressView *prg = (UIProgressView *)[self viewWithTag:kSDWebImageProgressView];
	prg.hidden = YES;
}

- (void)updateProgressView:(NSNumber *)progress {
	if ([progress floatValue] > 0) {
		UIProgressView *prg = nil;
		if ([self viewWithTag:kSDWebImageProgressView] == nil) {
			CGRect r = CGRectMake(10, (self.frame.size.height / 2) - 10, self.frame.size.width - 20, 30);
			prg = [[UIProgressView alloc] initWithFrame:r];
			prg.tag = kSDWebImageProgressView;
			prg.progressViewStyle = kSDWebImageProgressViewStyle;
			
			[self addSubview:prg];
			[prg release];
		} else {
			prg = (UIProgressView *)[self viewWithTag:kSDWebImageProgressView];
		}
		[prg setHidden:NO];
		[prg setProgress:[progress floatValue]];
	}
}


@end
