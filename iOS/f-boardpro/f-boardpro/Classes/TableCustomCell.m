//
//  TableCustomCell.m
//  TwitterBoard
//
//  Created by GLB-254 on 4/18/15.
//   All rights reserved.
//

#import "TableCustomCell.h"
#import "UIImageView+WebCache.h"
@implementation TableCustomCell

- (void)awakeFromNib {
    // Initialization code
}
- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if(self)
    {
        if([reuseIdentifier isEqualToString:@"Follow"])
        {
            self.userImage=[[UIImageView alloc]init];
            self.userImage.frame=CGRectMake(20, 20, 50, 50);
    
            // self.userImage.frame=CGRectMake(300, 20, 50, 50);
            self.userImage.layer.cornerRadius=10;
            self.userImage.layer.masksToBounds = YES;
            [self.contentView addSubview:self.userImage];
            
            self.fromUserImage=[[UIImageView alloc]init];
             self.fromUserImage.userInteractionEnabled = YES;
            self.fromUserImage.frame=CGRectMake(0, 0, 30, 30);
            
            self.fromUserImage.layer.cornerRadius=self.fromUserImage.frame.size.width/2;
            self.fromUserImage.layer.masksToBounds = YES;
            [self.contentView addSubview:self.fromUserImage];
            
            self.userNameDesc=[[UILabel alloc]init];
            self.userNameDesc.text=@"My feeds";
            self.userNameDesc.numberOfLines = 0;
            self.userNameDesc.font=[UIFont fontWithName:@"Helvetica-Bold" size:12];
            self.userNameDesc.frame=CGRectMake(100, 0, 220, 100);
            [self.contentView addSubview:self.userNameDesc];
            
            
            self.likeCountLabel = [[UILabel alloc]init];
//            self.likeCountLabel.text=@"Like count=10";
            self.likeCountLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:10];
            self.likeCountLabel.frame=CGRectMake(20, 230, 100, 50);
             [self.contentView addSubview:self.likeCountLabel];
            
            self.commentCountLabel = [[UILabel alloc]init];
            self.commentCountLabel.text=@"comment count=10";
            self.commentCountLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:10];
            self.commentCountLabel.frame=CGRectMake(120, 230, 100, 50);
            // [self.contentView addSubview:self.commentCountLabel];
            
        }
        if([reuseIdentifier isEqualToString:@"Display"])
        {
            self.userImage=[[UIImageView alloc]init];
            self.userImage.frame=CGRectMake(20, 40, 180, 180);
                //            self.userImage.layer.cornerRadius=20;
            [self.contentView addSubview:self.userImage];
            
            self.fromUserImage=[[UIImageView alloc]init];
            self.fromUserImage.userInteractionEnabled = YES;
            self.fromUserImage.frame=CGRectMake(0, 0, 30, 30);
                //            self.fromUserImage.layer.cornerRadius=20;
                //            self.fromUserImage.layer.masksToBounds = YES;
            [self.contentView addSubview:self.fromUserImage];
            
            self.fromLabel=[[UILabel alloc]init];
            self.fromLabel.numberOfLines = 0;
            self.fromLabel.frame=CGRectMake(10, 0, 300, 40);
                //            self.fromLabel.textColor = [UIColor blueColor];
            self.fromLabel.font=[UIFont fontWithName:@"Baskerville-Bold" size:20];
            [self.contentView addSubview:self.fromLabel];
            
            
            self.userNameDesc=[[UILabel alloc]init];
            self.userNameDesc.text=@"My feeds";
            self.userNameDesc.numberOfLines = 0;
            self.userNameDesc.font=[UIFont fontWithName:@"AvenirNextCondensed-Medium" size:18];
            self.userNameDesc.frame=CGRectMake(220, 60, 220, 200);
            [self.contentView addSubview:self.userNameDesc];
            
            
            self.likeCountLabel = [[UILabel alloc]init];
            self.likeCountLabel.text=@"Like count=10";
            self.likeCountLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:10];
            self.likeCountLabel.frame=CGRectMake(20, 230, 100, 50);
                //   [self.contentView addSubview:self.likeCountLabel];
            
            self.commentCountLabel = [[UILabel alloc]init];
            self.commentCountLabel.text=@"comment count=10";
            self.commentCountLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:10];
            self.commentCountLabel.frame=CGRectMake(120, 230, 100, 50);
                // [self.contentView addSubview:self.commentCountLabel];
        }
        
        if([reuseIdentifier isEqualToString:@"Myfeed"])
        {
            self.userImage=[[UIImageView alloc]init];
            self.userImage.frame=CGRectMake(20, 100, 100, 100);
                // self.userImage.frame=CGRectMake(300, 20, 50, 50);
            self.userImage.layer.cornerRadius=20;
            self.userImage.layer.masksToBounds = YES;
            [self.contentView addSubview:self.userImage];
            
            self.fromUserImage=[[UIImageView alloc]init];
            self.fromUserImage.userInteractionEnabled = YES;
            self.fromUserImage.frame=CGRectMake(10, 10, 50, 50);
            self.fromUserImage.layer.cornerRadius=self.fromUserImage.frame.size.width/2;
//            self.fromUserImage.image = [UIImage imageNamed:@"hel.jpeg"];
            [self.contentView addSubview:self.fromUserImage];
            
            self.userNameLabel=[[UILabel alloc]init];
//            self.userNameLabel.text=@"Vinayaka S Yattinahalli";
                //self.userNameLabel.numberOfLines = 0;
            self.userNameLabel.font=[UIFont fontWithName:@"Helvetica-Bold" size:15];
            self.userNameLabel.frame=CGRectMake(67, 0, 250, 40);
            [self.contentView addSubview:self.userNameLabel];
            
            self.timeLabel=[[UILabel alloc]init];
//            self.timeLabel.text=@"12:12:12:12 UTC";
            self.timeLabel.numberOfLines = 0;
            [self.timeLabel setTextColor:[UIColor grayColor]];
            self.timeLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:12];
            self.timeLabel.frame=CGRectMake(67, 21, 160, 40);
            [self.contentView addSubview:self.timeLabel];
            
            
            self.userNameDesc=[[UILabel alloc]init];
            self.userNameDesc.text=@"My feeds";
            self.userNameDesc.numberOfLines = 0;
            self.userNameDesc.font=[UIFont fontWithName:@"HelveticaNeue" size:15];
           self.userNameDesc.frame=CGRectMake(20, 60, self.frame.size.width, 40);
            [self.contentView addSubview:self.userNameDesc];
            
            
            
           
            
//            UIBarButtonItem *commn = [[UIBarButtonItem alloc]  initWithImage:[UIImage imageNamed:@"place.png"] style:UIBarButtonItemStylePlain target:self action:@selector(addLocation)];
//            
//            [barItems addObject:location];

            
        }
        if([reuseIdentifier isEqualToString:@"Display1"])
        {
            self.userImage=[[UIImageView alloc]init];
            self.userImage.frame=CGRectMake(20, 40, 120, 150);
                //            self.userImage.layer.cornerRadius=20;
            [self.contentView addSubview:self.userImage];
            
            self.fromUserImage=[[UIImageView alloc]init];
            self.fromUserImage.userInteractionEnabled = YES;
            self.fromUserImage.frame=CGRectMake(0, 0, 30, 30);
                //            self.fromUserImage.layer.cornerRadius=20;
                //            self.fromUserImage.layer.masksToBounds = YES;
            [self.contentView addSubview:self.fromUserImage];
            
            self.fromLabel=[[UILabel alloc]init];
            self.fromLabel.numberOfLines = 0;
            self.fromLabel.frame=CGRectMake(50, 0, 200, 40);
                //            self.fromLabel.textColor = [UIColor blueColor];
            self.fromLabel.font=[UIFont fontWithName:@"Baskerville-Bold" size:20];
            [self.contentView addSubview:self.fromLabel];
            
            
            self.userNameDesc=[[UILabel alloc]init];
            self.userNameDesc.text=@"My feeds";
            self.userNameDesc.numberOfLines = 0;
            self.userNameDesc.font=[UIFont fontWithName:@"AvenirNextCondensed-Medium" size:18];
            self.userNameDesc.frame=CGRectMake(160, 30, self.frame.size.width-170, 200);
            [self.contentView addSubview:self.userNameDesc];
            
            
            self.likeCountLabel = [[UILabel alloc]init];
            self.likeCountLabel.text=@"Like count=10";
            self.likeCountLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:10];
            self.likeCountLabel.frame=CGRectMake(20, 230, 100, 50);
            [self.contentView addSubview:self.likeCountLabel];
            
            self.commentCountLabel = [[UILabel alloc]init];
            self.commentCountLabel.text=@"comment count=10";
            self.commentCountLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:10];
            self.commentCountLabel.frame=CGRectMake(120, 230, 100, 50);
            [self.contentView addSubview:self.commentCountLabel];
        }
        

    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
